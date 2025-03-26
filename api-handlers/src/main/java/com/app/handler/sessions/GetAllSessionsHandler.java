package com.app.handler.sessions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.model.Session;
import com.app.repository.SessionRepository;
import com.app.util.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class GetAllSessionsHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final SessionRepository sessionRepository = new SessionRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            List<Session> sessions = sessionRepository.getAllSessions();

            // Check if sessions list is empty
            if (sessions.isEmpty()) {
                return new ApiResponse(404, "No sessions found");
            }

            // Use ObjectMapper to convert sessions to JSON string
            String jsonResponse = objectMapper.writeValueAsString(sessions);

            return new ApiResponse(200, jsonResponse);
        } catch (JsonProcessingException e) {
            // Specific handling for JSON serialization errors
            return new ApiResponse(500, "Error serializing sessions: " + e.getMessage());
        } catch (Exception e) {
            // General exception handling
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
