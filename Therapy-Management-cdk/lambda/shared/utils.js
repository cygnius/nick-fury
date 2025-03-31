const AWS = require("aws-sdk");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");
const dynamodb = new AWS.DynamoDB.DocumentClient();
const USERS_TABLE = process.env.USERS_TABLE;

const generateResponse = (statusCode, body) => {
  return {
    statusCode,
    headers: {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Credentials": true,
    },
    body: JSON.stringify(body),
  };
};

const validateRegisterInput = (name, email, password, contact) => {
  const errors = {};

  if (name.trim() === "") errors.name = "Name must not be empty";

  if (email.trim() === "") {
    errors.email = "Email must not be empty";
  } else {
    const regEx =
      /^([0-9a-zA-Z]([-.\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\w]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,9})$/;
    if (!email.match(regEx)) errors.email = "Invalid email address";
  }

  if (password === "") {
    errors.password = "Password must not be empty";
  } else if (password.length < 6) {
    errors.password = "Password must be at least 6 characters";
  }

  if (contact.trim() === "") errors.contact = "Contact must not be empty";

  return {
    errors,
    valid: Object.keys(errors).length < 1,
  };
};

const authenticateRequest = async (event) => {
  try {
    const token = event.headers.Authorization || event.headers.authorization;
    if (!token)
      return generateResponse(401, { error: "Authorization token required" });

    const decoded = jwt.verify(token.split(" ")[1], process.env.JWT_SECRET_KEY);

    const user = await dynamodb
      .get({
        TableName: USERS_TABLE,
        Key: { user_id: decoded.userId },
      })
      .promise();

    if (!user.Item) return generateResponse(404, { error: "User not found" });

    return {
      statusCode: 200,
      user: user.Item,
    };
  } catch (error) {
    console.error("Authentication error:", error);
    return generateResponse(401, { error: "Invalid or expired token" });
  }
};

const generateToken = (userId, userType) => {
  return jwt.sign({ userId, userType }, process.env.JWT_SECRET_KEY, {
    expiresIn: "24h",
  });
};

const comparePassword = async (password, hash) => {
  return await bcrypt.compare(password, hash);
};

module.exports = {
  generateResponse,
  validateRegisterInput,
  authenticateRequest,
  generateToken,
  comparePassword,
};
