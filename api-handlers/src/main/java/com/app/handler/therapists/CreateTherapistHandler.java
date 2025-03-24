package com.app.handler.therapists;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.model.Therapist;
import com.app.repository.TherapistRepository;
import com.app.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

public class CreateTherapistHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final TherapistRepository therapistRepository = new TherapistRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            // Parse request body
            Map<String, String> body = objectMapper.readValue((String) event.get("body"), Map.class);
            String therapistName = body.get("therapistName");
            String therapistEmail = body.get("therapistEmail");
            String therapistPassword = body.get("therapistPassword"); // Should be hashed before storing
            String specialization = body.get("specialization");

            if (therapistName == null || therapistEmail == null || therapistPassword == null || specialization == null) {
                return new ApiResponse(400, "Missing required fields");
            }

            // Generate UUID for therapistId
            String therapistId = UUID.randomUUID().toString();

            // Create therapist object
            Therapist therapist = new Therapist(therapistId, therapistName, therapistEmail, therapistPassword, specialization);

            // Store in DynamoDB
            therapistRepository.saveTherapist(therapist);

            // Success response
            return new ApiResponse(201, objectMapper.writeValueAsString(therapist));
        } catch (Exception e) {
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
