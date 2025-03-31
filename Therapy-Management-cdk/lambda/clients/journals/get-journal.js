const AWS = require("aws-sdk");
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

    const result = await dynamodb.query({
      TableName: JOURNAL_TABLE,
      IndexName: "ClientTimeIndex",
      KeyConditionExpression: "client_id = :clientId",
      ExpressionAttributeValues: {
        ":clientId": clientId,
      },
      ScanIndexForward: false,
    }).promise();

    const journals = result.Items.map(item => ({
      id: item.journal_id,
      clientId: item.client_id,
      title: item.title,
      content: item.content,
      emotionRating: item.emotion_rating,
      tags: item.tags,
      createdAt: item.created_at,
      updatedAt: item.updated_at,
    }));

    return generateResponse(200, journals);
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};