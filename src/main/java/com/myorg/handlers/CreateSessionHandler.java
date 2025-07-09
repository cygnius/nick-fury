package com.myorg.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.*;
import com.google.gson.*;
import com.myorg.models.Session;
import com.myorg.repository.SessionRepository;
import com.myorg.utils.ResponseBuilder;

public class CreateSessionHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final SessionRepository repo = new SessionRepository();
    private final Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            String therapistId = request.getPathParameters().get("therapist_id");
            Session session = gson.fromJson(request.getBody(), Session.class);
            session.setTherapistId(therapistId);

            Session created = repo.createSession(session);
            return ResponseBuilder.success(201, gson.toJson(created));
        } catch (Exception e) {
            context.getLogger().log("CreateSession error: " + e.getMessage());
            return ResponseBuilder.error(500, "Internal Server Error");
        }
    }
}
