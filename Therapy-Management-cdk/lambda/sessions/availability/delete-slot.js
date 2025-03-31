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

    await dynamodb.delete({
      TableName: AVAILABILITY_TABLE,
      Key: {
        therapist_id: therapistId,
        slot_id: slotId,
      },
      ConditionExpression: "attribute_exists(slot_id)",
    }).promise();

    return generateResponse(204, {});
  } catch (error) {
    if (error.code === "ConditionalCheckFailedException") {
      return generateResponse(404, { error: "Slot not found" });
    }
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};