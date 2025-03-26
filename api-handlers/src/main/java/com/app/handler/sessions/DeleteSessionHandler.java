package com.app.handler.sessions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.repository.SessionRepository;
import com.app.util.ApiResponse;

import java.util.Map;

public class DeleteSessionHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final SessionRepository sessionRepository = new SessionRepository();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            Map<String, String> pathParams = (Map<String, String>) event.get("pathParameters");
            String sessionId = pathParams.get("sessionId");
            String sessionDate = pathParams.get("sessionDate");

            if (sessionId == null || sessionDate == null) {
                return new ApiResponse(400, "Missing required parameters");
            }

//            sessionRepository.deleteSession(sessionId, sessionDate);
//            return new ApiResponse(204, "Session deleted successfully");

            boolean deleted = sessionRepository.deleteSession(sessionId, sessionDate);
            if (!deleted) {
                return new ApiResponse(404, "Session not found");
            }

            return new ApiResponse(200, "Session deleted successfully");

        } catch (Exception e) {
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
