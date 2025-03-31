const AWS = require("aws-sdk");
const { v4: uuidv4 } = require("uuid");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const MAPPING_TABLE = process.env.MAPPING_TABLE;
const THERAPISTS_TABLE = process.env.THERAPISTS_TABLE;
const CLIENTS_TABLE = process.env.CLIENTS_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { therapistId, clientId } = JSON.parse(event.body);

    if (!therapistId || !clientId) {
      return generateResponse(400, { error: "Therapist ID and client ID are required" });
    }

    const therapist = await dynamodb.get({
      TableName: THERAPISTS_TABLE,
      Key: { therapist_id: therapistId },
    }).promise();

    if (!therapist.Item) {
      return generateResponse(404, { error: "Therapist not found" });
    }

    const client = await dynamodb.get({
      TableName: CLIENTS_TABLE,
      Key: { client_id: clientId },
    }).promise();

    if (!client.Item) {
      return generateResponse(404, { error: "Client not found" });
    }

    const existingMapping = await dynamodb.query({
      TableName: MAPPING_TABLE,
      IndexName: "TherapistClientIndex",
      KeyConditionExpression: "therapist_id = :therapistId AND client_id = :clientId",
      ExpressionAttributeValues: {
        ":therapistId": therapistId,
        ":clientId": clientId,
      },
    }).promise();

    if (existingMapping.Items && existingMapping.Items.length > 0) {
      return generateResponse(409, { error: "Mapping request already exists" });
    }

    const mappingId = uuidv4();
    const now = new Date().toISOString();

    const mapping = {
      mapping_id: mappingId,
      therapist_id: therapistId,
      client_id: clientId,
      journal_access: false,
      status: "PENDING",
      created_at: now,
      updated_at: now,
    };

    await dynamodb.put({
      TableName: MAPPING_TABLE,
      Item: mapping,
    }).promise();

    return generateResponse(201, {
      id: mappingId,
      therapistId,
      clientId,
      journalAccess: mapping.journal_access,
      status: mapping.status,
      createdAt: now,
      updatedAt: now,
    });
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};