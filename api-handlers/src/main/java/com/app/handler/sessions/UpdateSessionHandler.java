package com.app.handler.sessions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.model.Session;
import com.app.repository.SessionRepository;
import com.app.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Optional;

public class UpdateSessionHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final SessionRepository sessionRepository = new SessionRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            Map<String, String> body = objectMapper.readValue((String) event.get("body"), Map.class);
            String sessionId = body.get("sessionId");
            String sessionDate = body.get("sessionDate");
            String sharedNotes = body.getOrDefault("sharedNotes", "");
            String privateNotes = body.getOrDefault("privateNotes", "");

            if (sessionId == null || sessionDate == null) {
                return new ApiResponse(400, "Missing required fields");
            }

            Optional<Session> sessionOpt = sessionRepository.getSessionById(sessionId, sessionDate);
            if (sessionOpt.isEmpty()) {
                return new ApiResponse(404, "Session not found");
            }

            Session updatedSession = sessionOpt.get();
            updatedSession.setSharedNotes(sharedNotes);
            updatedSession.setPrivateNotes(privateNotes);

            sessionRepository.updateSession(updatedSession);
            return new ApiResponse(200, objectMapper.writeValueAsString(updatedSession));
        } catch (Exception e) {
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
