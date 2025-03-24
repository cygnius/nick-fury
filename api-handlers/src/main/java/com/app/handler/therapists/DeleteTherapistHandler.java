package com.app.handler.therapists;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.repository.TherapistRepository;
import com.app.util.ApiResponse;

import java.util.Map;

public class DeleteTherapistHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final TherapistRepository therapistRepository = new TherapistRepository();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            // Extract therapistId from path parameters
            Map<String, String> pathParameters = (Map<String, String>) event.get("pathParameters");
            if (pathParameters == null || !pathParameters.containsKey("therapistId")) {
                return new ApiResponse(400, "Missing therapistId in path parameters");
            }

            String therapistId = pathParameters.get("therapistId");

            // Delete client from repository
//            therapistRepository.deleteTherapist(therapistId);
//            return new ApiResponse(204, "Therapist deleted successfully");

            boolean deleted = therapistRepository.deleteTherapist(therapistId);
            if (!deleted) {
                return new ApiResponse(404, "Therapist not found");
            }
            return new ApiResponse(200, "Therapist deleted");
        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}