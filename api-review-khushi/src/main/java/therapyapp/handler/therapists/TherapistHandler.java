package therapyapp.handler.therapists;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.HashMap;
import java.util.Map;

public class TherapistHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String path = (String) input.get("path");
        String httpMethod = (String) input.get("httpMethod");
        Map<String, Object> response = new HashMap<>();

        if ("GET".equals(httpMethod) && path.matches("/therapists/[a-zA-Z0-9-]+/clients")) {
            response.put("statusCode", 200);
            response.put("body", "List of clients for therapist.");
        } else if ("PUT".equals(httpMethod) && path.matches("/therapists/[a-zA-Z0-9-]+/clients/[a-zA-Z0-9-]+/journal-access")) {
            response.put("statusCode", 200);
            response.put("body", "Journal access updated.");
        } else if ("POST".equals(httpMethod) && path.equals("/therapists/map-client")) {
            response.put("statusCode", 201);
            response.put("body", "Client mapped to therapist.");
        } else {
            response.put("statusCode", 400);
            response.put("body", "Invalid input or method.");
        }
        return response;
    }
}
