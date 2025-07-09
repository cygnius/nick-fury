package com.myorg.utils;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class ResponseBuilder {

    public static APIGatewayProxyResponseEvent success(int statusCode, String body) {
        return new APIGatewayProxyResponseEvent()
            .withStatusCode(statusCode)
            .withHeaders(java.util.Map.of("Content-Type", "application/json"))
            .withBody(body);
    }

    public static APIGatewayProxyResponseEvent error(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent()
            .withStatusCode(statusCode)
            .withHeaders(java.util.Map.of("Content-Type", "application/json"))
            .withBody("{\"error\": \"" + message + "\"}");
    }
}
