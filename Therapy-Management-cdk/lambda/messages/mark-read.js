const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const MESSAGES_TABLE = process.env.MESSAGES_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { messageId } = event.pathParameters;

    const result = await dynamodb.update({
      TableName: MESSAGES_TABLE,
      Key: { message_id: messageId },
      UpdateExpression: "SET is_read = :isRead",
      ConditionExpression: "attribute_exists(message_id)",
      ExpressionAttributeValues: {
        ":isRead": true,
      },
      ReturnValues: "ALL_NEW",
    }).promise();

    return generateResponse(200, {
      id: messageId,
      senderId: result.Attributes.sender_id,
      receiverId: result.Attributes.receiver_id,
      content: result.Attributes.content,
      sentAt: result.Attributes.sent_at,
      isRead: result.Attributes.is_read,
    });
  } catch (error) {
    if (error.code === "ConditionalCheckFailedException") {
      return generateResponse(404, { error: "Message not found" });
    }
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};