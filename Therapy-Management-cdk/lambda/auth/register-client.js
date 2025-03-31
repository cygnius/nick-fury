const AWS = require("aws-sdk");
const bcrypt = require("bcryptjs");
const { v4: uuidv4 } = require("uuid");
const { generateResponse, validateRegisterInput } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const USERS_TABLE = process.env.USERS_TABLE;
const THERAPISTS_TABLE = process.env.THERAPISTS_TABLE;

exports.handler = async (event) => {
  try {
    const { name, email, password, contact, qualifications, specialization } = JSON.parse(event.body);

    const { valid, errors } = validateRegisterInput(name, email, password, contact);
    if (!valid) {
      return generateResponse(400, { errors });
    }

    const existingUser = await dynamodb.scan({
      TableName: USERS_TABLE,
      FilterExpression: "email = :email",
      ExpressionAttributeValues: { ":email": email },
    }).promise();

    if (existingUser.Items?.length > 0) {
      return generateResponse(409, { error: "Email already exists" });
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    const userId = uuidv4();
    const therapistId = uuidv4();

    await dynamodb.transactWrite({
      TransactItems: [
        {
          Put: {
            TableName: USERS_TABLE,
            Item: {
              user_id: userId,
              name,
              email,
              password_hash: hashedPassword,
              contact,
              user_type: "THERAPIST",
              created_at: new Date().toISOString(),
            },
            ConditionExpression: "attribute_not_exists(user_id)",
          },
        },
        {
          Put: {
            TableName: THERAPISTS_TABLE,
            Item: {
              therapist_id: therapistId,
              user_id: userId,
              qualifications,
              specialization,
            },
            ConditionExpression: "attribute_not_exists(therapist_id)",
          },
        },
      ],
    }).promise();

    return generateResponse(201, {
      id: userId,
      name,
      email,
      contact,
      qualifications,
      specialization,
      userType: "THERAPIST",
    });
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};