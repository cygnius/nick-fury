const AWS = require("aws-sdk");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");
const { generateResponse, comparePassword } = require("../shared/utils");

const dynamodb = new AWS.DynamoDB.DocumentClient();
const USERS_TABLE = process.env.USERS_TABLE;

exports.handler = async (event) => {
  try {
    const { email, password } = JSON.parse(event.body);

    if (!email || !password) {
      return generateResponse(400, {
        error: "Email and password are required",
      });
    }

    const user = await dynamodb
      .scan({
        TableName: USERS_TABLE,
        FilterExpression: "email = :email",
        ExpressionAttributeValues: { ":email": email },
      })
      .promise();

    if (!user.Items?.length) {
      return generateResponse(401, { error: "Invalid credentials" });
    }

    const userData = user.Items[0];
    const isMatch = await comparePassword(password, userData.password_hash);

    if (!isMatch) {
      return generateResponse(401, { error: "Invalid credentials" });
    }

    const token = jwt.sign(
      { userId: userData.user_id, userType: userData.user_type },
      process.env.JWT_SECRET_KEY,
      { expiresIn: "24h" }
    );

    return generateResponse(200, {
      token,
      userId: userData.user_id,
      userType: userData.user_type,
      expiresAt: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(),
    });
  } catch (error) {
    console.error("Error:", error);
    return generateResponse(500, { error: "Internal Server Error" });
  }
};
