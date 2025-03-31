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

    const { userId } = event.pathParameters;
    const { status, startDate, endDate } = event.queryStringParameters || {};

    const queryParams = {
      TableName: SESSION_TABLE,
      IndexName: authResponse.user.userType === "CLIENT" ? "ClientTimeIndex" : "TherapistTimeIndex",
      KeyConditionExpression: `${authResponse.user.userType === "CLIENT" ? "client_id" : "therapist_id"} = :userId`,
      ExpressionAttributeValues: {
        ":userId": userId,
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

    if (startDate || endDate) {
      if (!queryParams.FilterExpression) {
        queryParams.FilterExpression = "";
      } else {
        queryParams.FilterExpression += " AND ";
      }

      if (startDate && endDate) {
        queryParams.FilterExpression += "start_time BETWEEN :startDate AND :endDate";
        queryParams.ExpressionAttributeValues[":startDate"] = startDate;
        queryParams.ExpressionAttributeValues[":endDate"] = endDate;
      } else if (startDate) {
        queryParams.FilterExpression += "start_time >= :startDate";
        queryParams.ExpressionAttributeValues[":startDate"] = startDate;
      } else if (endDate) {
        queryParams.FilterExpression += "start_time <= :endDate";
        queryParams.ExpressionAttributeValues[":endDate"] = endDate;
      }
    }

    const result = await dynamodb.query(queryParams).promise();

    const appointments = result.Items.map(item => ({
      id: item.session_id,
      therapistId: item.therapist_id,
      clientId: item.client_id,
      startTime: item.start_time,
      endTime: item.end_time,
      status: item.status,
      notes: item.notes,
      createdAt: item.created_at,
    }));

    return generateResponse(200, appointments);
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};