package com.app.handler.mappings;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.model.Mapping;
import com.app.repository.MappingRepository;
import com.app.util.ApiResponse;

import java.util.Map;

public class CreateMappingHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
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

            // Validate existence of client and therapist
            if (!mappingRepository.clientAndTherapistExist(clientId, therapistId)) {
                return new ApiResponse(404, "Client or Therapist not found.");
            }

            // Create mapping entry
            Mapping mapping = new Mapping(clientId, therapistId, "No");

            // Save in DynamoDB
            mappingRepository.saveMapping(mapping);

            return new ApiResponse(201, "Mapping created successfully.");

        } catch (Exception e) {
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
