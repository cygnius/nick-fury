package com.myorg.handlers;

import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.*;
import com.google.gson.*;
import com.myorg.models.Mapping;
import com.myorg.repository.MappingRepository;
import com.myorg.utils.ResponseBuilder;

import java.util.Map;

public class CreateMappingHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final MappingRepository repo = new MappingRepository();
    private final Gson gson = new Gson();

    private static class Body {
        boolean journalAccess = false;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        Map<String, String> pathParams = request.getPathParameters();

        if (pathParams == null || !pathParams.containsKey("client_id") || !pathParams.containsKey("therapist_id")) {
            return ResponseBuilder.error(400, "Missing client_id or therapist_id");
        }

        Body body = gson.fromJson(request.getBody(), Body.class);
        Mapping mapping = repo.createMapping(pathParams.get("client_id"), pathParams.get("therapist_id"), body.journalAccess);

        return ResponseBuilder.success(201, gson.toJson(Map.of("mapping_id", mapping.getId())));
    }
}
