package com.myorg.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.*;
import com.google.gson.*;
import com.myorg.models.Session;
import com.myorg.repository.SessionRepository;
import com.myorg.utils.ResponseBuilder;
import java.util.List;




public class ListSessionsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final SessionRepository repo = new SessionRepository();
    private final Gson gson = new Gson();

    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            String therapistId = request.getPathParameters().get("therapist_id");
            List<Session> sessions = repo.listSessionsForTherapist(therapistId);
            return ResponseBuilder.success(200, gson.toJson(sessions));
        } catch (Exception e) {
            context.getLogger().log("ListSessions error: " + e.getMessage());
            return ResponseBuilder.error(500, "Internal Server Error");
        }
    }
}
