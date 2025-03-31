const AWS = require("aws-sdk");
const { v4: uuidv4 } = require("uuid");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const MESSAGES_TABLE = process.env.MESSAGES_TABLE;
const USERS_TABLE = process.env.USERS_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { senderId } = authResponse.user;
    const { receiverId, content } = JSON.parse(event.body);

    if (!receiverId || !content) {
      return generateResponse(400, { error: "Receiver ID and content are required" });
    }

    const receiver = await dynamodb.get({
      TableName: USERS_TABLE,
      Key: { user_id: receiverId },
    }).promise();

    if (!receiver.Item) {
      return generateResponse(404, { error: "Receiver not found" });
    }

    const messageId = uuidv4();
    const now = new Date().toISOString();

    const message = {
      message_id: messageId,
      sender_id: senderId,
      receiver_id: receiverId,
      content,
      sent_at: now,
      is_read: false,
    };

    await dynamodb.put({
      TableName: MESSAGES_TABLE,
      Item: message,
    }).promise();

    return generateResponse(201, {
      id: messageId,
      senderId,
      receiverId,
      content,
      sentAt: now,
      isRead: false,
    });
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};