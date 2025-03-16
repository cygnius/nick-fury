package therapyapp.handler.messaging;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

import java.util.*;

public class MessagingHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private static final String TABLE_NAME = "Messages";
    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
    private final Table messagesTable = dynamoDB.getTable(TABLE_NAME);

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String httpMethod = (String) input.get("httpMethod");
        Map<String, Object> response = new HashMap<>();

        try {
            if ("POST".equalsIgnoreCase(httpMethod)) {
                Map<String, Object> body = (Map<String, Object>) input.get("body");
                String senderId = (String) body.get("senderId");
                String receiverId = (String) body.get("receiverId");
                String messageText = (String) body.get("message");

                if (senderId == null || receiverId == null || messageText == null) {
                    throw new IllegalArgumentException("Invalid input data. Ensure senderId, receiverId, and message are provided.");
                }

                String conversationId = senderId + "_" + receiverId;
                long timestamp = System.currentTimeMillis();

                Item messageItem = new Item()
                        .withPrimaryKey("conversationId", conversationId, "timestamp", timestamp)
                        .withString("senderId", senderId)
                        .withString("receiverId", receiverId)
                        .withString("message", messageText);

                messagesTable.putItem(messageItem);

                response.put("statusCode", 201);
                response.put("body", "Message sent successfully.");
            }

            else if ("GET".equalsIgnoreCase(httpMethod)) {
                Map<String, String> queryParams = (Map<String, String>) input.get("queryStringParameters");
                String senderId = queryParams.get("senderId");

                if (senderId == null) {
                    throw new IllegalArgumentException("Missing senderId in query parameters.");
                }

                QuerySpec querySpec = new QuerySpec()
                        .withKeyConditionExpression("senderId = :senderId")
                        .withValueMap(new ValueMap().withString(":senderId", senderId));

                List<Map<String, Object>> messages = new ArrayList<>();
                for (Item item : messagesTable.query(querySpec)) {
                    Map<String, Object> message = new HashMap<>();
                    message.put("conversationId", item.getString("conversationId"));
                    message.put("timestamp", item.getLong("timestamp"));
                    message.put("message", item.getString("message"));
                    messages.add(message);
                }

                response.put("statusCode", 200);
                response.put("body", messages);
            }

            else {
                response.put("statusCode", 405);
                response.put("body", "Method not allowed.");
            }

        } catch (Exception e) {
            response.put("statusCode", 400);
            response.put("body", "Error: " + e.getMessage());
        }

        return response;
    }
}
