const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const SESSION_TABLE = process.env.SESSION_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { clientId } = event.pathParameters;
    const { status } = event.queryStringParameters || {};

    const queryParams = {
      TableName: SESSION_TABLE,
      IndexName: "ClientTimeIndex",
      KeyConditionExpression: "client_id = :clientId",
      ExpressionAttributeValues: {
        ":clientId": clientId,
      },
      ScanIndexForward: false,
    };

    if (status) {
      queryParams.FilterExpression = "#status = :status";
      queryParams.ExpressionAttributeNames = {
        "#status": "status",
      };
      queryParams.ExpressionAttributeValues[":status"] = status;
    }

    const result = await dynamodb.query(queryParams).promise();

    const sessions = result.Items.map(item => ({
      id: item.session_id,
      therapistId: item.therapist_id,
      clientId: item.client_id,
      startTime: item.start_time,
      endTime: item.end_time,
      status: item.status,
      notes: item.notes,
      createdAt: item.created_at,
      updatedAt: item.updated_at,
    }));

    return generateResponse(200, sessions);
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};