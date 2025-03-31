const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const MAPPING_TABLE = process.env.MAPPING_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { clientId, mappingId } = event.pathParameters;
    const { status } = JSON.parse(event.body);

    if (!status || !["APPROVED", "REJECTED"].includes(status)) {
      return generateResponse(400, { error: "Invalid status. Must be 'APPROVED' or 'REJECTED'" });
    }

    const now = new Date().toISOString();

    const result = await dynamodb.update({
      TableName: MAPPING_TABLE,
      Key: { mapping_id: mappingId },
      UpdateExpression: "SET #status = :status, updated_at = :now",
      ConditionExpression: "attribute_exists(mapping_id) AND client_id = :clientId",
      ExpressionAttributeNames: {
        "#status": "status",
      },
      ExpressionAttributeValues: {
        ":status": status,
        ":now": now,
        ":clientId": clientId,
      },
      ReturnValues: "ALL_NEW",
    }).promise();

    return generateResponse(200, {
      id: mappingId,
      therapistId: result.Attributes.therapist_id,
      clientId: result.Attributes.client_id,
      journalAccess: result.Attributes.journal_access,
      status: result.Attributes.status,
      createdAt: result.Attributes.created_at,
      updatedAt: result.Attributes.updated_at,
    });
  } catch (error) {
    if (error.code === "ConditionalCheckFailedException") {
      return generateResponse(404, { error: "Mapping not found" });
    }
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};