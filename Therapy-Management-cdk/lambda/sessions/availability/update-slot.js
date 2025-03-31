const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const AVAILABILITY_TABLE = process.env.AVAILABILITY_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { therapistId, slotId } = event.pathParameters;
    const { startTime, endTime, notes } = JSON.parse(event.body);

    if (!startTime || !endTime) {
      return generateResponse(400, { error: "Start time and end time are required" });
    }

    const now = new Date().toISOString();

    const result = await dynamodb.update({
      TableName: AVAILABILITY_TABLE,
      Key: {
        therapist_id: therapistId,
        slot_id: slotId,
      },
      UpdateExpression: "SET start_time = :startTime, end_time = :endTime, notes = :notes, updated_at = :now",
      ConditionExpression: "attribute_exists(slot_id)",
      ExpressionAttributeValues: {
        ":startTime": startTime,
        ":endTime": endTime,
        ":notes": notes || "",
        ":now": now,
      },
      ReturnValues: "ALL_NEW",
    }).promise();

    return generateResponse(200, {
      id: slotId,
      therapistId,
      startTime: result.Attributes.start_time,
      endTime: result.Attributes.end_time,
      notes: result.Attributes.notes,
      status: result.Attributes.status,
      createdAt: result.Attributes.created_at,
      updatedAt: result.Attributes.updated_at,
    });
  } catch (error) {
    if (error.code === "ConditionalCheckFailedException") {
      return generateResponse(404, { error: "Slot not found" });
    }
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};