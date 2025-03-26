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

public class GetSessionsByTherapistHandler implements RequestHandler<Map<String, Object>, ApiResponse> {

    private final SessionRepository sessionRepository = new SessionRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            // Null check for path parameters
            Map<String, String> pathParams = (Map<String, String>) event.get("pathParameters");
            if (pathParams == null) {
                return new ApiResponse(400, "No path parameters provided");
            }

            String therapistId = pathParams.get("therapistId");
            String sessionDate = pathParams.get("sessionDate");

            // Validate parameters
            if (therapistId == null || therapistId.trim().isEmpty()) {
                return new ApiResponse(400, "Invalid or missing therapistId");
            }

            if (sessionDate == null || sessionDate.trim().isEmpty()) {
                return new ApiResponse(400, "Invalid or missing sessionDate");
            }

            // Fetch sessions
            List<Session> sessions = sessionRepository.getSessionsByTherapistAndDate(therapistId, sessionDate);

            // Handle empty results
            if (sessions.isEmpty()) {
                return new ApiResponse(204, "No sessions found for the given therapist and date");
            }

            // Convert to JSON and return
            return new ApiResponse(200, objectMapper.writeValueAsString(sessions));
        } catch (JsonProcessingException e) {
            // Specific handling for JSON serialization errors
            return new ApiResponse(500, "Error serializing sessions: " + e.getMessage());
        } catch (Exception e) {
            // General exception handling
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
