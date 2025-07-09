package com.myorg.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.google.gson.Gson;
import com.myorg.models.Message;
import com.myorg.repository.MessageRepository;
import com.myorg.utils.ResponseBuilder;

public class SendMessageHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final MessageRepository repo = new MessageRepository();
    private final Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            Message msg = gson.fromJson(request.getBody(), Message.class);

            if (msg.getSender_id() == null || msg.getRecipient_id() == null || msg.getContent() == null) {
                return ResponseBuilder.error(400, "Missing required fields");
            }

            repo.saveMessage(msg);
            return ResponseBuilder.success(201, gson.toJson(msg));

        } catch (Exception e) {
            return ResponseBuilder.error(500, e.getMessage());
        }
    }
}
