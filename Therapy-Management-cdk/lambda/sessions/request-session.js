const AWS = require("aws-sdk");
const { v4: uuidv4 } = require("uuid");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const AVAILABILITY_TABLE = process.env.AVAILABILITY_TABLE;
const SESSION_TABLE = process.env.SESSION_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { availabilitySlotId, clientId, notes } = JSON.parse(event.body);

    if (!availabilitySlotId || !clientId) {
      return generateResponse(400, { error: "Availability slot ID and client ID are required" });
    }

    const slotResult = await dynamodb.scan({
      TableName: AVAILABILITY_TABLE,
      FilterExpression: "slot_id = :slotId AND status = :status",
      ExpressionAttributeValues: {
        ":slotId": availabilitySlotId,
        ":status": "AVAILABLE",
      },
    }).promise();

    if (!slotResult.Items || slotResult.Items.length === 0) {
      return generateResponse(404, { error: "Availability slot not found or already booked" });
    }

    const slot = slotResult.Items[0];
    const sessionId = uuidv4();
    const now = new Date().toISOString();

    await dynamodb.transactWrite({
      TransactItems: [
        {
          Put: {
            TableName: SESSION_TABLE,
            Item: {
              session_id: sessionId,
              therapist_id: slot.therapist_id,
              client_id: clientId,
              start_time: slot.start_time,
              end_time: slot.end_time,
              status: "REQUESTED",
              notes: notes || "",
              created_at: now,
              updated_at: now,
            },
          },
        },
        {
          Update: {
            TableName: AVAILABILITY_TABLE,
            Key: {
              therapist_id: slot.therapist_id,
              slot_id: slot.slot_id,
            },
            UpdateExpression: "SET status = :status, updated_at = :now",
            ConditionExpression: "attribute_exists(slot_id) AND status = :available",
            ExpressionAttributeValues: {
              ":status": "BOOKED",
              ":now": now,
              ":available": "AVAILABLE",
            },
          },
        },
      ],
    }).promise();

    return generateResponse(201, {
      id: sessionId,
      therapistId: slot.therapist_id,
      clientId,
      startTime: slot.start_time,
      endTime: slot.end_time,
      status: "REQUESTED",
      notes: notes || "",
      createdAt: now,
      updatedAt: now,
    });
  } catch (error) {
    if (error.code === "TransactionCanceledException") {
      return generateResponse(409, { error: "Slot already booked" });
    }
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};