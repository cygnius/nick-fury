package com.myorg.handlers;

import com.amazonaws.services.lambda.runtime.Context;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.*;
import com.google.gson.*;
import com.myorg.models.Session;
import com.myorg.repository.SessionRepository;
import com.myorg.utils.ResponseBuilder;

public class UpdateSessionHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final SessionRepository repo = new SessionRepository();
    private final Gson gson = new Gson();

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            String sessionId = request.getPathParameters().get("session_id");
            Session updatedData = gson.fromJson(request.getBody(), Session.class);
            boolean success = repo.updateSession(sessionId, updatedData);
            if (!success) return ResponseBuilder.error(404, "Session not found");

            return ResponseBuilder.success(200, "Session updated successfully");
        } catch (Exception e) {
            context.getLogger().log("UpdateSession error: " + e.getMessage());
            return ResponseBuilder.error(500, "Internal Server Error");
        }
    }
}
