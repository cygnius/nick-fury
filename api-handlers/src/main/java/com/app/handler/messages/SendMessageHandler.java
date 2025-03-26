package com.app.handler.messages;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.repository.MessageRepository;
import com.app.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class SendMessageHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final MessageRepository messageRepository = new MessageRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            Map<String, String> body = objectMapper.readValue((String) event.get("body"), Map.class);
            String senderId = body.get("senderId");
            String receiverId = body.get("receiverId");
            String messageContent = body.get("messageContent");

            if (senderId == null || receiverId == null || messageContent == null) {
                return new ApiResponse(400, "Missing required fields");
            }

            messageRepository.sendMessage(senderId, receiverId, messageContent);
            return new ApiResponse(200, "Message sent successfully");
        } catch (Exception e) {
            return new ApiResponse(500, "Error: " + e.getMessage());
        }
    }
}
