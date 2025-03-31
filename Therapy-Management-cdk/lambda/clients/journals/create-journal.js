const AWS = require("aws-sdk");
const { v4: uuidv4 } = require("uuid");
const { generateResponse, authenticateRequest } = require("../../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const JOURNAL_TABLE = process.env.JOURNAL_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { clientId } = event.pathParameters;
    const { title, content, emotionRating, tags } = JSON.parse(event.body);

    if (!title || !content || !emotionRating) {
      return generateResponse(400, { error: "Title, content and emotionRating are required" });
    }

    if (emotionRating < 1 || emotionRating > 10) {
      return generateResponse(400, { error: "Emotion rating must be between 1 and 10" });
    }

    const journalId = uuidv4();
    const now = new Date().toISOString();

    const journalEntry = {
      journal_id: journalId,
      client_id: clientId,
      title,
      content,
      emotion_rating: emotionRating,
      tags: tags || [],
      created_at: now,
      updated_at: now,
    };

    await dynamodb.put({
      TableName: JOURNAL_TABLE,
      Item: journalEntry,
    }).promise();

    return generateResponse(201, {
      id: journalId,
      clientId,
      title,
      content,
      emotionRating,
      tags: journalEntry.tags,
      createdAt: now,
      updatedAt: now,
    });
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};