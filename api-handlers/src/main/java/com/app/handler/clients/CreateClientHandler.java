package com.app.handler.clients;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.model.Client;
import com.app.repository.ClientRepository;
import com.app.util.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

public class CreateClientHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final ClientRepository clientRepository = new ClientRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            // Parse request body
            Map<String, String> body = objectMapper.readValue((String) event.get("body"), Map.class);
            String clientName = body.get("clientName");
            String clientEmail = body.get("clientEmail");
            String clientPassword = body.get("clientPassword"); // Should be hashed before storing

            if (clientName == null || clientEmail == null || clientPassword == null) {
                return new ApiResponse(400, "Missing required fields");
            }

            // Generate UUID for clientId
            String clientId = UUID.randomUUID().toString();

            // Create client object
            Client client = new Client(clientId, clientName, clientEmail, clientPassword);

            // Store in DynamoDB
            clientRepository.saveClient(client);

            // Success response
            return new ApiResponse(201, objectMapper.writeValueAsString(client));

        } catch (Exception e) {
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
