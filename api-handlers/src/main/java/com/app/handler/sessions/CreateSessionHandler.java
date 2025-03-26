package com.app.handler.sessions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.model.Session;
import com.app.repository.SessionRepository;
import com.app.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class CreateSessionHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final SessionRepository sessionRepository = new SessionRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            Map<String, String> body = objectMapper.readValue((String) event.get("body"), Map.class);
            String therapistId = body.get("therapistId");
            String clientId = body.get("clientId");
            String sharedNotes = body.getOrDefault("sharedNotes", "");
            String privateNotes = body.getOrDefault("privateNotes", "");

            if (therapistId == null || clientId == null) {
                return new ApiResponse(400, "Missing required fields");
            }

            String sessionId = UUID.randomUUID().toString();
            String sessionDate = Instant.now().toString(); // ISO 8601 format

            Session session = new Session(sessionId, sessionDate, therapistId, clientId, sharedNotes, privateNotes);
            sessionRepository.saveSession(session);

            return new ApiResponse(201, objectMapper.writeValueAsString(session));
        } catch (Exception e) {
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
