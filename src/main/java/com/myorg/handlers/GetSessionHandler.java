package com.myorg.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.myorg.models.Session;
import com.myorg.utils.ResponseBuilder;
import com.myorg.repository.SessionRepository;


public class GetSessionHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final SessionRepository repo = new SessionRepository();
    private final Gson gson = new Gson();

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            String sessionId = request.getPathParameters().get("session_id");
            Session session = repo.getSessionById(sessionId);
            if (session == null) {
                return ResponseBuilder.error(404, "Session not found");
            }
            return ResponseBuilder.success(200, gson.toJson(session));
        } catch (Exception e) {
            context.getLogger().log("GetSession error: " + e.getMessage());
            return ResponseBuilder.error(500, "Internal Server Error");
        }
    }
}
