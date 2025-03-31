const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const NOTES_TABLE = process.env.NOTES_TABLE;
const JOURNAL_TABLE = process.env.JOURNAL_TABLE;
const MESSAGES_TABLE = process.env.MESSAGES_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { query, type, startDate, endDate } = event.queryStringParameters || {};

    if (!query) {
      return generateResponse(400, { error: "Query parameter is required" });
    }

    const searchTypes = type ? type.split(",") : ["notes", "journals", "messages"];
    const results = {};

    if (searchTypes.includes("notes")) {
      const notesParams = {
        TableName: NOTES_TABLE,
        FilterExpression: "contains(content, :query)",
        ExpressionAttributeValues: {
          ":query": query,
        },
      };

      if (startDate || endDate) {
        notesParams.FilterExpression += " AND ";
        if (startDate && endDate) {
          notesParams.FilterExpression += "created_at BETWEEN :startDate AND :endDate";
          notesParams.ExpressionAttributeValues[":startDate"] = startDate;
          notesParams.ExpressionAttributeValues[":endDate"] = endDate;
        } else if (startDate) {
          notesParams.FilterExpression += "created_at >= :startDate";
          notesParams.ExpressionAttributeValues[":startDate"] = startDate;
        } else if (endDate) {
          notesParams.FilterExpression += "created_at <= :endDate";
          notesParams.ExpressionAttributeValues[":endDate"] = endDate;
        }
      }

      const notesResult = await dynamodb.scan(notesParams).promise();
      results.notes = notesResult.Items.map(item => ({
        id: item.note_id,
        sessionId: item.session_id,
        therapistId: item.therapist_id,
        clientId: item.client_id,
        content: item.content,
        isPrivate: item.is_private,
        createdAt: item.created_at,
      }));
    }

    if (searchTypes.includes("journals")) {
      const journalsParams = {
        TableName: JOURNAL_TABLE,
        FilterExpression: "contains(title, :query) OR contains(content, :query)",
        ExpressionAttributeValues: {
          ":query": query,
        },
      };

      if (startDate || endDate) {
        journalsParams.FilterExpression += " AND ";
        if (startDate && endDate) {
          journalsParams.FilterExpression += "created_at BETWEEN :startDate AND :endDate";
          journalsParams.ExpressionAttributeValues[":startDate"] = startDate;
          journalsParams.ExpressionAttributeValues[":endDate"] = endDate;
        } else if (startDate) {
          journalsParams.FilterExpression += "created_at >= :startDate";
          journalsParams.ExpressionAttributeValues[":startDate"] = startDate;
        } else if (endDate) {
          journalsParams.FilterExpression += "created_at <= :endDate";
          journalsParams.ExpressionAttributeValues[":endDate"] = endDate;
        }
      }

      const journalsResult = await dynamodb.scan(journalsParams).promise();
      results.journals = journalsResult.Items.map(item => ({
        id: item.journal_id,
        clientId: item.client_id,
        title: item.title,
        content: item.content,
        emotionRating: item.emotion_rating,
        tags: item.tags,
        createdAt: item.created_at,
      }));
    }

    if (searchTypes.includes("messages")) {
      const messagesParams = {
        TableName: MESSAGES_TABLE,
        FilterExpression: "contains(content, :query) AND (sender_id = :userId OR receiver_id = :userId)",
        ExpressionAttributeValues: {
          ":query": query,
          ":userId": authResponse.user.user_id,
        },
      };

      if (startDate || endDate) {
        messagesParams.FilterExpression += " AND ";
        if (startDate && endDate) {
          messagesParams.FilterExpression += "sent_at BETWEEN :startDate AND :endDate";
          messagesParams.ExpressionAttributeValues[":startDate"] = startDate;
          messagesParams.ExpressionAttributeValues[":endDate"] = endDate;
        } else if (startDate) {
          messagesParams.FilterExpression += "sent_at >= :startDate";
          messagesParams.ExpressionAttributeValues[":startDate"] = startDate;
        } else if (endDate) {
          messagesParams.FilterExpression += "sent_at <= :endDate";
          messagesParams.ExpressionAttributeValues[":endDate"] = endDate;
        }
      }

      const messagesResult = await dynamodb.scan(messagesParams).promise();
      results.messages = messagesResult.Items.map(item => ({
        id: item.message_id,
        senderId: item.sender_id,
        receiverId: item.receiver_id,
        content: item.content,
        sentAt: item.sent_at,
        isRead: item.is_read,
      }));
    }

    return generateResponse(200, results);
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};