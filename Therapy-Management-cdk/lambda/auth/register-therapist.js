const AWS = require('aws-sdk');
const { v4: uuidv4 } = require('uuid');
const bcrypt = require('bcryptjs');
const { validateRegisterInput, generateResponse } = require('../shared/utils');

const dynamodb = new AWS.DynamoDB.DocumentClient();
const USERS_TABLE = process.env.USERS_TABLE;
const THERAPISTS_TABLE = process.env.THERAPISTS_TABLE;

exports.handler = async (event) => {
  try {
    const { name, email, password, contact, qualifications, specialization } = JSON.parse(event.body);

    const { valid, errors } = validateRegisterInput(name, email, password, contact);
    if (!valid) return generateResponse(400, { errors });

    const userCheck = await dynamodb.scan({
      TableName: USERS_TABLE,
      FilterExpression: 'email = :email',
      ExpressionAttributeValues: { ':email': email }
    }).promise();

    if (userCheck.Items?.length > 0) {
      return generateResponse(409, { error: 'Email already exists' });
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    const userId = uuidv4();
    const therapistId = uuidv4();
    const timestamp = new Date().toISOString();

    await dynamodb.transactWrite({
      TransactItems: [
        {
          Put: {
            TableName: USERS_TABLE,
            Item: {
              user_id: userId,
              name,
              email,
              contact,
              password_hash: hashedPassword,
              user_type: 'THERAPIST',
              created_at: timestamp,
              updated_at: timestamp
            }
          }
        },
        {
          Put: {
            TableName: THERAPISTS_TABLE,
            Item: {
              therapist_id: therapistId,
              user_id: userId,
              qualifications,
              specialization,
              created_at: timestamp,
              updated_at: timestamp
            }
          }
        }
      ]
    }).promise();

    return generateResponse(201, {
      user_id: userId,
      therapist_id: therapistId,
      name,
      email,
      contact,
      qualifications,
      specialization,
      user_type: 'THERAPIST'
    });
  } catch (error) {
    console.error('Error:', error);
    return generateResponse(500, { error: 'Internal Server Error' });
  }
};