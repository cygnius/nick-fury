package com.myorg.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import com.google.gson.Gson;
import com.myorg.models.Message;
import com.myorg.repository.MessageRepository;
import com.myorg.utils.ResponseBuilder;

import java.util.List;
import java.util.Map;

//public class GetMessagesHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
//
//    private final MessageRepository repo = new MessageRepository();
//    private final Gson gson = new Gson();
//
//    @Override
//    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
//        try {
//            String senderId = request.getPathParameters().get("senderId");
//            String recipientId = request.getPathParameters().get("recipientId");
//
//            if (senderId == null || recipientId == null) {
//                return ResponseBuilder.error(400, "Missing senderId or recipientId in path");
//            }
//
//            List<Message> messages = repo.getMessageHistory(senderId, recipientId);
//            return ResponseBuilder.success(200, gson.toJson(messages));
//
//        } catch (Exception e) {
//            return ResponseBuilder.error(500, e.getMessage());
//        }
//    }
//}




public class GetMessagesHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final MessageRepository repo = new MessageRepository();
    private final Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            Map<String, String> pathParams = request.getPathParameters();

            if (pathParams == null || !pathParams.containsKey("senderId") || !pathParams.containsKey("recipientId")) {
                return ResponseBuilder.error(400, "Missing senderId or recipientId in path");
            }

            String senderId = pathParams.get("senderId");
            String recipientId = pathParams.get("recipientId");

            context.getLogger().log("Fetching messages between " + senderId + " and " + recipientId);

            List<Message> messages = repo.getMessageHistory(senderId, recipientId);
            return ResponseBuilder.success(200, gson.toJson(messages));

        } catch (Exception e) {
            context.getLogger().log("ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseBuilder.error(500, "Internal Server Error: " + e.getMessage());
        }
    }
}

