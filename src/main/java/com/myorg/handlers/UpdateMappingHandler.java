package com.myorg.handlers;

import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.*;
import com.google.gson.*;
import com.myorg.repository.MappingRepository;
import com.myorg.utils.ResponseBuilder;

import java.util.Map;

public class UpdateMappingHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final MappingRepository repo = new MappingRepository();
    private final Gson gson = new Gson();

    private static class Body {
        boolean journalAccess;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        Map<String, String> pathParams = request.getPathParameters();

        if (pathParams == null || !pathParams.containsKey("client_id") || !pathParams.containsKey("therapist_id")) {
            return ResponseBuilder.error(400, "Missing client_id or therapist_id");
        }

        Body body = gson.fromJson(request.getBody(), Body.class);
        boolean updated = repo.updateMapping(pathParams.get("client_id"), pathParams.get("therapist_id"), body.journalAccess);

        if (!updated) return ResponseBuilder.error(404, "Mapping not found");
        return ResponseBuilder.success(200, "Mapping updated");
    }
}
