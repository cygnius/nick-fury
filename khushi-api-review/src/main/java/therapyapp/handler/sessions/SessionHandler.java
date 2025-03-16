package therapyapp.handler.sessions;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.HashMap;
import java.util.Map;

public class SessionHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String path = (String) input.get("path");
        String httpMethod = (String) input.get("httpMethod");
        Map<String, Object> response = new HashMap<>();

        if ("POST".equals(httpMethod) && path.equals("/sessions")) {
            response.put("statusCode", 201);
            response.put("body", "Session created successfully.");
        } else if ("PUT".equals(httpMethod) && path.matches("/sessions/[a-zA-Z0-9-]+/notes")) {
            response.put("statusCode", 200);
            response.put("body", "Notes updated.");
        } else {
            response.put("statusCode", 400);
            response.put("body", "Invalid input or method.");
        }
        return response;
    }
}
