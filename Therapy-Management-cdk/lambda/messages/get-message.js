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

    const { userId } = event.pathParameters;
    const { startDate, endDate, unreadOnly } = event.queryStringParameters || {};

    const queryParams = {
      TableName: MESSAGES_TABLE,
      IndexName: "ReceiverSenderIndex",
      KeyConditionExpression: "receiver_id = :userId",
      ExpressionAttributeValues: {
        ":userId": userId,
      },
      ScanIndexForward: false,
    };

    if (startDate || endDate) {
      queryParams.FilterExpression = [];
      queryParams.ExpressionAttributeValues = {};

      if (startDate) {
        queryParams.FilterExpression.push("sent_at >= :startDate");
        queryParams.ExpressionAttributeValues[":startDate"] = startDate;
      }

      if (endDate) {
        queryParams.FilterExpression.push("sent_at <= :endDate");
        queryParams.ExpressionAttributeValues[":endDate"] = endDate;
      }

      queryParams.FilterExpression = queryParams.FilterExpression.join(" AND ");
    }

    if (unreadOnly === "true") {
      if (queryParams.FilterExpression) {
        queryParams.FilterExpression += " AND is_read = :isRead";
      } else {
        queryParams.FilterExpression = "is_read = :isRead";
      }
      queryParams.ExpressionAttributeValues[":isRead"] = false;
    }

    const result = await dynamodb.query(queryParams).promise();

    const messages = result.Items.map(item => ({
      id: item.message_id,
      senderId: item.sender_id,
      receiverId: item.receiver_id,
      content: item.content,
      sentAt: item.sent_at,
      isRead: item.is_read,
    }));

    return generateResponse(200, messages);
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};