const AWS = require("aws-sdk");
const { v4: uuidv4 } = require("uuid");
const { generateResponse, authenticateRequest } = require("../../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const NOTES_TABLE = process.env.NOTES_TABLE;
const SESSION_TABLE = process.env.SESSION_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { sessionId } = event.pathParameters;
    const { content, isPrivate } = JSON.parse(event.body);

    if (!content) {
      return generateResponse(400, { error: "Content is required" });
    }

    const session = await dynamodb.get({
      TableName: SESSION_TABLE,
      Key: { session_id: sessionId },
    }).promise();

    if (!session.Item) {
      return generateResponse(404, { error: "Session not found" });
    }

    const noteId = uuidv4();
    const now = new Date().toISOString();

    const note = {
      note_id: noteId,
      session_id: sessionId,
      therapist_id: session.Item.therapist_id,
      client_id: session.Item.client_id,
      content,
      is_private: !!isPrivate,
      created_at: now,
      updated_at: now,
    };

    await dynamodb.put({
      TableName: NOTES_TABLE,
      Item: note,
    }).promise();

    return generateResponse(201, {
      id: noteId,
      sessionId,
      therapistId: note.therapist_id,
      clientId: note.client_id,
      content,
      isPrivate: note.is_private,
      createdAt: now,
      updatedAt: now,
    });
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};