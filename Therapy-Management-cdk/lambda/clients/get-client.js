const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const USERS_TABLE = process.env.USERS_TABLE;
const CLIENTS_TABLE = process.env.CLIENTS_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { clientId } = event.pathParameters;

    const clientData = await dynamodb.get({
      TableName: CLIENTS_TABLE,
      Key: { client_id: clientId },
    }).promise();

    if (!clientData.Item) {
      return generateResponse(404, { error: "Client not found" });
    }

    const userData = await dynamodb.get({
      TableName: USERS_TABLE,
      Key: { user_id: clientData.Item.user_id },
    }).promise();

    if (!userData.Item) {
      return generateResponse(404, { error: "User not found" });
    }

    return generateResponse(200, {
      id: userData.Item.user_id,
      name: userData.Item.name,
      email: userData.Item.email,
      contact: userData.Item.contact,
      userType: userData.Item.user_type,
      createdAt: userData.Item.created_at,
    });
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};