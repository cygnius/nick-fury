const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const MAPPING_TABLE = process.env.MAPPING_TABLE;
const THERAPISTS_TABLE = process.env.THERAPISTS_TABLE;
const USERS_TABLE = process.env.USERS_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { clientId } = event.pathParameters;

    const mappings = await dynamodb.query({
      TableName: MAPPING_TABLE,
      IndexName: "ClientTherapistIndex",
      KeyConditionExpression: "client_id = :clientId",
      FilterExpression: "#status = :status",
      ExpressionAttributeNames: {
        "#status": "status",
      },
      ExpressionAttributeValues: {
        ":clientId": clientId,
        ":status": "APPROVED",
      },
    }).promise();

    if (!mappings.Items || mappings.Items.length === 0) {
      return generateResponse(200, []);
    }

    const therapistPromises = mappings.Items.map(async (mapping) => {
      const therapistData = await dynamodb.get({
        TableName: THERAPISTS_TABLE,
        Key: { therapist_id: mapping.therapist_id },
      }).promise();

      if (therapistData.Item) {
        const userData = await dynamodb.get({
          TableName: USERS_TABLE,
          Key: { user_id: therapistData.Item.user_id },
        }).promise();

        if (userData.Item) {
          return {
            id: userData.Item.user_id,
            name: userData.Item.name,
            email: userData.Item.email,
            contact: userData.Item.contact,
            qualifications: therapistData.Item.qualifications,
            specialization: therapistData.Item.specialization,
            userType: userData.Item.user_type,
            createdAt: userData.Item.created_at,
          };
        }
      }
      return null;
    });

    const therapists = (await Promise.all(therapistPromises)).filter(therapist => therapist !== null);

    return generateResponse(200, therapists);
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};