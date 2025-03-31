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
        ":status": "PENDING",
      },
    }).promise();

    if (!mappings.Items || mappings.Items.length === 0) {
      return generateResponse(200, []);
    }

    const mappingPromises = mappings.Items.map(async (mapping) => {
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
            mappingId: mapping.mapping_id,
            therapistId: mapping.therapist_id,
            clientId: mapping.client_id,
            therapistName: userData.Item.name,
            therapistEmail: userData.Item.email,
            therapistContact: userData.Item.contact,
            qualifications: therapistData.Item.qualifications,
            specialization: therapistData.Item.specialization,
            journalAccess: mapping.journal_access,
            status: mapping.status,
            createdAt: mapping.created_at,
          };
        }
      }
      return null;
    });

    const results = (await Promise.all(mappingPromises)).filter(mapping => mapping !== null);

    return generateResponse(200, results);
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};