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

    const { therapistId } = event.pathParameters;

    const result = await dynamodb.query({
      TableName: AVAILABILITY_TABLE,
      KeyConditionExpression: "therapist_id = :therapistId",
      ExpressionAttributeValues: {
        ":therapistId": therapistId,
      },
      ScanIndexForward: true,
    }).promise();

    const slots = result.Items.map(item => ({
      id: item.slot_id,
      therapistId: item.therapist_id,
      startTime: item.start_time,
      endTime: item.end_time,
      notes: item.notes,
      status: item.status,
      createdAt: item.created_at,
      updatedAt: item.updated_at,
    }));

    return generateResponse(200, slots);
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};