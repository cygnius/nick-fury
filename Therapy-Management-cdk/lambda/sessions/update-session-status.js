const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const SESSION_TABLE = process.env.SESSION_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { sessionId } = event.pathParameters;
    const { status } = JSON.parse(event.body);

    if (!status || !["ACCEPTED", "REJECTED"].includes(status)) {
      return generateResponse(400, { error: "Invalid status. Must be 'ACCEPTED' or 'REJECTED'" });
    }

    const now = new Date().toISOString();

    const result = await dynamodb.update({
      TableName: SESSION_TABLE,
      Key: { session_id: sessionId },
      UpdateExpression: "SET #status = :status, updated_at = :now",
      ConditionExpression: "attribute_exists(session_id)",
      ExpressionAttributeNames: {
        "#status": "status",
      },
      ExpressionAttributeValues: {
        ":status": status,
        ":now": now,
      },
      ReturnValues: "ALL_NEW",
    }).promise();

    return generateResponse(200, {
      id: sessionId,
      therapistId: result.Attributes.therapist_id,
      clientId: result.Attributes.client_id,
      startTime: result.Attributes.start_time,
      endTime: result.Attributes.end_time,
      status: result.Attributes.status,
      notes: result.Attributes.notes,
      createdAt: result.Attributes.created_at,
      updatedAt: result.Attributes.updated_at,
    });
  } catch (error) {
    if (error.code === "ConditionalCheckFailedException") {
      return generateResponse(404, { error: "Session not found" });
    }
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};