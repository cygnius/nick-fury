package com.app.handler.mappings;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.repository.MappingRepository;
import com.app.util.ApiResponse;

import java.util.Map;

public class DeleteMappingHandler implements RequestHandler<Map<String, Object>, ApiResponse> {

    private final MappingRepository mappingRepository = new MappingRepository();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            // Extract path parameters
            Map<String, String> pathParameters = (Map<String, String>) event.get("pathParameters");
            String clientId = pathParameters.get("clientId");
            String therapistId = pathParameters.get("therapistId");

            if (clientId == null || therapistId == null) {
                return new ApiResponse(400, "Missing clientId or therapistId in the path.");
            }

//            mappingRepository.deleteMapping(clientId, therapistId);
//            return new ApiResponse(200, "Mapping deleted successfully.");

            boolean deleted = mappingRepository.deleteMapping(clientId, therapistId);
            if (!deleted) {
                return new ApiResponse(404, "Mapping not found");
            }
            return new ApiResponse(200, "Mapping deleted successfully");
        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
