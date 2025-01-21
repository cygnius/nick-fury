# Therapy Journaling App

A simple Spring Boot application for managing therapy journaling, built with DynamoDB for backend storage and JWT authentication for secure user access.

## Prerequisites

Before running the project, make sure you have the following installed:

- [Java 11 or later](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [DynamoDB Local](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html) (for local DynamoDB setup)
- [AWS SDK credentials](https://docs.aws.amazon.com/IAM/latest/UserGuide/access-keys.html) (if using AWS DynamoDB instead of local)

## Setup and Configuration

### 1. **Clone the Repository**

Clone the repository to your local machine:
```bash
git clone https://github.com/YashVardhan3/therapy-app.git
cd therapy-app
```
### 2. Configure application.properties
In the `src/main/resources/application.properties` file, update the following properties to match your environment:

```properties
spring.application.name=therapy
# DynamoDB Local URL (if using local DynamoDB)
amazon.dynamodb.endpoint=http://localhost:8000/

# AWS DynamoDB credentials (if using AWS DynamoDB instead of local)
amazon.aws.accesskey=your-access-key
amazon.aws.secretkey=your-secret-key
aws.dynamodb.region=us-east-1

# JWT Secret and Expiration Configuration
app.jwtSecret=your-jwt-secret-key
app.jwtExpirationMs=86400000  # 24 hours
app.jwtRefreshExpirationMs=604800000  # 7 days

# Optionally, enable detailed logging
# logging.level.org.springframework=DEBUG
# logging.level.com.amazonaws=DEBUG
```

**Notes:**
- Replace `your-access-key` and `your-secret-key` with your actual AWS credentials if you are using AWS DynamoDB.
- If you're running DynamoDB locally, the URL should be `http://localhost:8000/`. Ensure that DynamoDB Local is running.

### 3. Run DynamoDB Local (if using local DynamoDB)
To run DynamoDB locally, follow the steps in the official documentation.

Here’s how to run it in the background (assuming you have DynamoDB Local downloaded):

```bash
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb
```

This will start DynamoDB on port 8000.

### 4. Run the Spring Boot Application
Once everything is configured, you can run your Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

Alternatively, you can build the project and run it as a JAR:

```bash
mvn clean package
java -jar target/therapy-app-0.0.1-SNAPSHOT.jar
```

### 5. Access the Application
Once the application starts, you can access it via the following URL in your browser:

```bash
http://localhost:8080
```

### 6. Testing the Endpoints
Use tools like Postman or cURL to test the API endpoints. Make sure to include the JWT token in the Authorization header when making requests to protected endpoints.

## How to Set Up JWT Authentication
This application uses JWT tokens for secure authentication. Here's how to obtain the JWT token:

Send a POST request to the login endpoint with the user's credentials:

```http
POST http://localhost:8080/login
Content-Type: application/json
{
  "email": "test@example.com",
  "password": "password"
}
```

The response will include a JWT token. You can use this token for future requests by including it in the Authorization header:

```http
Authorization: Bearer <jwt-token>
```

## Additional Information

### Local DynamoDB Setup
If you're running DynamoDB locally, ensure that the port 8000 is not being used by other services. You can change the port in `application.properties` if needed.

### Environment Variables
For better security and ease of configuration, you can set the AWS credentials and JWT secret as environment variables instead of directly in the `application.properties` file:

```bash
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_ACCESS_KEY=your-secret-key
export AWS_REGION=us-east-1
export JWT_SECRET_KEY=your-jwt-secret-key
```

Then modify the `application.properties` to use these environment variables:

```properties
amazon.aws.accesskey=${AWS_ACCESS_KEY_ID}
amazon.aws.secretkey=${AWS_SECRET_ACCESS_KEY}
aws.dynamodb.region=${AWS_REGION}
app.jwtSecret=${JWT_SECRET_KEY}
```

### Logs
You can enable debug logging to trace issues or see detailed logs of the application by uncommenting the logging lines in `application.properties`:

```properties
logging.level.org.springframework=DEBUG
logging.level.com.amazonaws=DEBUG
```

### Troubleshooting
- **DynamoDB Local not working:** Ensure that DynamoDB Local is properly started and accessible at the endpoint `http://localhost:8000/`.
- **JWT authentication issues:** Make sure the JWT secret and expiration times are configured correctly in the application.

## Postman Collection

You can find the Postman collection for this project in the following file:
- [Postman Collection](/Therapy%20App%20Endpoints.postman_collection.json)

Import this collection into Postman to interact with the API endpoints.

