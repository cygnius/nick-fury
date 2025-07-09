package com.myorg.handlers;

import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.*;
import com.myorg.repository.MappingRepository;
import com.myorg.utils.ResponseBuilder;

import java.util.Map;

public class DeleteMappingHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final MappingRepository repo = new MappingRepository();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        Map<String, String> pathParams = request.getPathParameters();

        if (pathParams == null || !pathParams.containsKey("client_id") || !pathParams.containsKey("therapist_id")) {
            return ResponseBuilder.error(400, "Missing client_id or therapist_id");
        }

        boolean deleted = repo.deleteMapping(pathParams.get("client_id"), pathParams.get("therapist_id"));
        if (!deleted) return ResponseBuilder.error(404, "Mapping not found");

        return ResponseBuilder.success(204, "");
    }
}
