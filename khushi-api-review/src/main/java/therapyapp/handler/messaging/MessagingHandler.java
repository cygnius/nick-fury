package therapyapp.handler.messaging;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.HashMap;
import java.util.Map;

public class MessagingHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String path = (String) input.get("path");
        String httpMethod = (String) input.get("httpMethod");
        Map<String, Object> response = new HashMap<>();

        if ("POST".equals(httpMethod) && path.equals("/messages")) {
            response.put("statusCode", 201);
            response.put("body", "Message sent successfully.");
        } else if ("GET".equals(httpMethod) && path.equals("/messages/history")) {
            response.put("statusCode", 200);
            response.put("body", "Message history retrieved.");
        } else {
            response.put("statusCode", 400);
            response.put("body", "Invalid input or method.");
        }
        return response;
    }
}