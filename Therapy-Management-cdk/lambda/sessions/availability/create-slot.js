const AWS = require("aws-sdk");
const { v4: uuidv4 } = require("uuid");
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
    const { startTime, endTime, notes } = JSON.parse(event.body);

    if (!startTime || !endTime) {
      return generateResponse(400, { error: "Start time and end time are required" });
    }

    const slotId = uuidv4();
    const now = new Date().toISOString();

    const slotData = {
      slot_id: slotId,
      therapist_id: therapistId,
      start_time: startTime,
      end_time: endTime,
      notes: notes || "",
      status: "AVAILABLE",
      created_at: now,
      updated_at: now,
    };

    await dynamodb.put({
      TableName: AVAILABILITY_TABLE,
      Item: slotData,
    }).promise();

    return generateResponse(201, {
      id: slotId,
      therapistId,
      startTime,
      endTime,
      notes: slotData.notes,
      status: slotData.status,
      createdAt: now,
      updatedAt: now,
    });
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};