package therapyapp.handler.auth;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import therapyapp.util.ResponseHelper;

public class AuthHandler {
    private final DynamoDbClient dynamoDbClient;
    private final String USERS_TABLE = "Users";

    public AuthHandler() {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(Region.EU_NORTH_1) 
                .build();
    }

    public void handleRequest(Map<String, Object> event, Map<String, Object> context) {
        String httpMethod = (String) event.get("httpMethod");
        String response;

        switch (httpMethod) {
            case "POST":
                if (event.get("path").equals("/register")) {
                    response = registerUser(event);
                } else if (event.get("path").equals("/login")) {
                    response = loginUser(event);
                } else {
                    response = ResponseHelper.generateResponse(404, "Endpoint not found");
                }
                break;
            default:
                response = ResponseHelper.generateResponse(405, "Method not allowed");
        }


        System.out.println(response);
    }

    private String registerUser(Map<String, Object> event) {
        try {
            Gson gson = new Gson();
            Map<String, String> requestBody = gson.fromJson((String) event.get("body"), Map.class);

            String username = requestBody.get("username");
            String passwordHash = requestBody.get("passwordHash");

            
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("username", AttributeValue.builder().s(username).build());
            item.put("passwordHash", AttributeValue.builder().s(passwordHash).build());

            PutItemRequest putItemRequest = PutItemRequest.builder()
                    .tableName(USERS_TABLE)
                    .item(item)
                    .build();

            dynamoDbClient.putItem(putItemRequest);

            return ResponseHelper.generateResponse(201, "User registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.generateResponse(500, "Error registering user");
        }
    }

    private String loginUser(Map<String, Object> event) {
        try {
            Gson gson = new Gson();
            Map<String, String> requestBody = gson.fromJson((String) event.get("body"), Map.class);

            String username = requestBody.get("username");
            String passwordHash = requestBody.get("passwordHash");

            
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .tableName(USERS_TABLE)
                    .key(Map.of("username", AttributeValue.builder().s(username).build()))
                    .build();

            GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);

            if (!getItemResponse.hasItem()) {
                return ResponseHelper.generateResponse(404, "User not found");
            }

            String storedPasswordHash = getItemResponse.item().get("passwordHash").s();

            if (storedPasswordHash.equals(passwordHash)) {
                return ResponseHelper.generateResponse(200, "Login successful");
            } else {
                return ResponseHelper.generateResponse(401, "Invalid credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseHelper.generateResponse(500, "Error during login");
        }
    }
}
