package therapyapp.handler.clients;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ClientHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String path = (String) input.get("path");
        String httpMethod = (String) input.get("httpMethod");
        Map<String, Object> response = new HashMap<>();

        if ("GET".equals(httpMethod) && path.startsWith("/clients")) {
            if (path.equals("/clients")) {
                response.put("statusCode", 200);
                response.put("body", "List of therapists.");
            } else if (path.matches("/clients/[a-zA-Z0-9-]+/therapists")) {
                response.put("statusCode", 200);
                response.put("body", "List of therapists for client.");
            } else if (path.matches("/clients/[a-zA-Z0-9-]+/journals")) {
                response.put("statusCode", 201);
                response.put("body", "Journal entry added.");
            } else {
                response.put("statusCode", 400);
                response.put("body", "Invalid input.");
            }
        } else {
            response.put("statusCode", 400);
            response.put("body", "Invalid endpoint or method.");
        }
        return response;
    }
}
