const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const NOTES_TABLE = process.env.NOTES_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { sessionId } = event.pathParameters;
    const { includePrivate = "false" } = event.queryStringParameters || {};

    const queryParams = {
      TableName: NOTES_TABLE,
      IndexName: "SessionTimeIndex",
      KeyConditionExpression: "session_id = :sessionId",
      ExpressionAttributeValues: {
        ":sessionId": sessionId,
      },
      ScanIndexForward: false,
    };

    if (includePrivate === "false") {
      queryParams.FilterExpression = "is_private = :isPrivate";
      queryParams.ExpressionAttributeValues[":isPrivate"] = false;
    }

    const result = await dynamodb.query(queryParams).promise();

    const notes = result.Items.map(item => ({
      id: item.note_id,
      sessionId: item.session_id,
      therapistId: item.therapist_id,
      clientId: item.client_id,
      content: item.content,
      isPrivate: item.is_private,
      createdAt: item.created_at,
      updatedAt: item.updated_at,
    }));

    return generateResponse(200, notes);
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};