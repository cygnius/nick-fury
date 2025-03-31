const AWS = require("aws-sdk");
const { generateResponse, authenticateRequest } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const MAPPING_TABLE = process.env.MAPPING_TABLE;

exports.handler = async (event) => {
  try {
    const authResponse = await authenticateRequest(event);
    if (authResponse.statusCode !== 200) {
      return authResponse;
    }

    const { clientId, mappingId } = event.pathParameters;
    const { journalAccess } = JSON.parse(event.body);

    if (typeof journalAccess !== "boolean") {
      return generateResponse(400, { error: "journalAccess must be a boolean" });
    }

    const now = new Date().toISOString();

    const result = await dynamodb.update({
      TableName: MAPPING_TABLE,
      Key: { mapping_id: mappingId },
      UpdateExpression: "SET journal_access = :journalAccess, updated_at = :now",
      ConditionExpression: "attribute_exists(mapping_id) AND client_id = :clientId AND #status = :status",
      ExpressionAttributeNames: {
        "#status": "status",
      },
      ExpressionAttributeValues: {
        ":journalAccess": journalAccess,
        ":now": now,
        ":clientId": clientId,
        ":status": "APPROVED",
      },
      ReturnValues: "ALL_NEW",
    }).promise();

    return generateResponse(200, {
      id: mappingId,
      therapistId: result.Attributes.therapist_id,
      clientId: result.Attributes.client_id,
      journalAccess: result.Attributes.journal_access,
      status: result.Attributes.status,
      createdAt: result.Attributes.created_at,
      updatedAt: result.Attributes.updated_at,
    });
  } catch (error) {
    if (error.code === "ConditionalCheckFailedException") {
      return generateResponse(404, { error: "Mapping not found or not approved" });
    }
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};