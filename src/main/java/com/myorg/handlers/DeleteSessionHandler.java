package com.myorg.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.*;
import com.myorg.repository.SessionRepository;
import com.myorg.utils.ResponseBuilder;


public class DeleteSessionHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final SessionRepository repo = new SessionRepository();

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            String sessionId = request.getPathParameters().get("session_id");
            boolean deleted = repo.deleteSession(sessionId);
            if (!deleted) {
                return ResponseBuilder.error(404, "Session not found");
            }
            return ResponseBuilder.success(200, "Session deleted successfully");
        } catch (Exception e) {
            context.getLogger().log("DeleteSession error: " + e.getMessage());
            return ResponseBuilder.error(500, "Internal Server Error");
        }
    }
}
