package com.app.handler.messages;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.model.Message;
import com.app.repository.MessageRepository;
import com.app.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class GetMessageBySenderAtTimeHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final MessageRepository messageRepository = new MessageRepository();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            Map<String, String> pathParams = (Map<String, String>) event.get("pathParameters");
            String senderId = pathParams.get("senderId");
            String timestamp = pathParams.get("timestamp");

            Message message = messageRepository.getMessageBySenderAtTime(senderId, timestamp);

            if (message == null) {
                return new ApiResponse(404, "No message found for the given sender and timestamp.");
            }

            String jsonResponse = objectMapper.writeValueAsString(message);
            return new ApiResponse(200, jsonResponse);

        } catch (Exception e) {
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
