const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const USERS_TABLE = process.env.USERS_TABLE;
const THERAPISTS_TABLE = process.env.THERAPISTS_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { therapistId } = event.pathParameters;

    const therapistData = await dynamodb.get({
      TableName: THERAPISTS_TABLE,
      Key: { therapist_id: therapistId },
    }).promise();

    if (!therapistData.Item) {
      return generateResponse(404, { error: "Therapist not found" });
    }

    const userData = await dynamodb.get({
      TableName: USERS_TABLE,
      Key: { user_id: therapistData.Item.user_id },
    }).promise();

    if (!userData.Item) {
      return generateResponse(404, { error: "User not found" });
    }

    return generateResponse(200, {
      id: userData.Item.user_id,
      name: userData.Item.name,
      email: userData.Item.email,
      contact: userData.Item.contact,
      qualifications: therapistData.Item.qualifications,
      specialization: therapistData.Item.specialization,
      userType: userData.Item.user_type,
      createdAt: userData.Item.created_at,
    });
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};