package com.app.handler.clients;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.app.repository.ClientRepository;
import com.app.util.ApiResponse;

import java.util.Map;

public class DeleteClientHandler implements RequestHandler<Map<String, Object>, ApiResponse> {
    private final ClientRepository clientRepository = new ClientRepository();

    @Override
    public ApiResponse handleRequest(Map<String, Object> event, Context context) {
        try {
            // Extract clientId from path parameters
            Map<String, String> pathParameters = (Map<String, String>) event.get("pathParameters");
            if (pathParameters == null || !pathParameters.containsKey("clientId")) {
                return new ApiResponse(400, "Missing clientId in path parameters");
            }

            String clientId = pathParameters.get("clientId");

            // Delete client from repository
//            clientRepository.deleteClient(clientId);
//            return new ApiResponse(204, "Client deleted successfully");

            boolean deleted = clientRepository.deleteClient(clientId);
            if (!deleted) {
                return new ApiResponse(404, "Client not found");
            }
            return new ApiResponse(200, "Client deleted successfully");
        } catch (Exception e) {
            System.err.println("Error processing request: " + e.getMessage());
            e.printStackTrace();
            return new ApiResponse(500, "Internal Server Error: " + e.getMessage());
        }
    }
}
