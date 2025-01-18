package therapyapp.handler.auth;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class AuthHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String path = (String) input.get("path");
        String httpMethod = (String) input.get("httpMethod");
        Map<String, Object> response = new HashMap<>();

        switch (path) {
            case "/auth/register":
                if ("POST".equals(httpMethod)) {
                    response.put("statusCode", 201);
                    response.put("body", "User created successfully.");
                } else {
                    response.put("statusCode", 400);
                    response.put("body", "Invalid request method.");
                }
                break;

            case "/auth/login":
                if ("POST".equals(httpMethod)) {
                    response.put("statusCode", 200);
                    response.put("body", "Login successful, returns JWT.");
                } else {
                    response.put("statusCode", 401);
                    response.put("body", "Unauthorized, invalid credentials.");
                }
                break;

            default:
                response.put("statusCode", 400);
                response.put("body", "Invalid endpoint.");
        }
        return response;
    }
}
