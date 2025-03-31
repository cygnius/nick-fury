const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const MAPPING_TABLE = process.env.MAPPING_TABLE;
const CLIENTS_TABLE = process.env.CLIENTS_TABLE;
const USERS_TABLE = process.env.USERS_TABLE;
const JOURNAL_TABLE = process.env.JOURNAL_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { therapistId } = event.pathParameters;
    const { keyword, minFeeling, maxFeeling } = event.queryStringParameters;

    const mappings = await dynamodb.query({
      TableName: MAPPING_TABLE,
      IndexName: "TherapistClientIndex",
      KeyConditionExpression: "therapist_id = :therapistId",
      ExpressionAttributeValues: {
        ":therapistId": therapistId,
      },
    }).promise();

    if (!mappings.Items || mappings.Items.length === 0) {
      return generateResponse(200, []);
    }

    const clientPromises = mappings.Items.map(async (mapping) => {
      const clientData = await dynamodb.get({
        TableName: CLIENTS_TABLE,
        Key: { client_id: mapping.client_id },
      }).promise();

      if (clientData.Item) {
        const userData = await dynamodb.get({
          TableName: USERS_TABLE,
          Key: { user_id: clientData.Item.user_id },
        }).promise();

        if (userData.Item) {
          const matchesKeyword = !keyword || 
            userData.Item.name.toLowerCase().includes(keyword.toLowerCase()) || 
            userData.Item.email.toLowerCase().includes(keyword.toLowerCase());

          let matchesFeeling = true;
          if (minFeeling || maxFeeling) {
            const journalEntries = await dynamodb.query({
              TableName: JOURNAL_TABLE,
              IndexName: "ClientTimeIndex",
              KeyConditionExpression: "client_id = :clientId",
              ExpressionAttributeValues: {
                ":clientId": clientData.Item.client_id,
              },
              Limit: 1,
              ScanIndexForward: false,
            }).promise();

            if (journalEntries.Items.length > 0) {
              const rating = journalEntries.Items[0].emotion_rating;
              matchesFeeling = 
                (!minFeeling || rating >= parseInt(minFeeling)) && 
                (!maxFeeling || rating <= parseInt(maxFeeling));
            } else {
              matchesFeeling = false;
            }
          }

          if (matchesKeyword && matchesFeeling) {
            return {
              id: userData.Item.user_id,
              name: userData.Item.name,
              email: userData.Item.email,
              contact: userData.Item.contact,
              userType: userData.Item.user_type,
              createdAt: userData.Item.created_at,
            };
          }
        }
      }
      return null;
    });

    const clients = (await Promise.all(clientPromises)).filter(client => client !== null);

    return generateResponse(200, clients);
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};