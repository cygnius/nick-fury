package com.app.handler.messages;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.model.Message;
import com.app.repository.MessageRepository;
import com.app.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.util.List;
import java.util.Map;

public class GetMessagesBetweenUsersHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final MessageRepository messageRepository = new MessageRepository();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            Map<String, String> pathParams = (Map<String, String>) event.get("pathParameters");
            String senderId = pathParams.get("senderId");
            String receiverId = pathParams.get("receiverId");

            List<Message> messages = messageRepository.getMessagesBetweenUsers(senderId, receiverId);

            if (messages.isEmpty()) {
                return new ApiResponse(404, "No messages found between the given users");
            }

            String jsonResponse = objectMapper.writeValueAsString(messages);

            return new ApiResponse(200, jsonResponse);

        } catch (Exception e) {
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
