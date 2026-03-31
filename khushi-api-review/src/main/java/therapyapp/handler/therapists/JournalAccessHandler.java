package therapyapp.handler.therapists;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import therapyapp.model.JournalAccess;

public class JournalAccessHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    private final Gson gson = new Gson();
    
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        
        Map<String, String> pathParams = (Map<String, String>) input.get("pathParameters");
        String clientId = pathParams.get("clientId");
        String therapistId = ((Map<String, String>) input.get("queryStringParameters")).get("therapistId");
        
        Map<String, Object> requestBody = gson.fromJson((String) input.get("body"), Map.class);
        String accessStatus = (String) requestBody.get("access"); // ACCEPTED, REJECTED, REVOKED
        
        JournalAccess journalAccess = new JournalAccess(therapistId, clientId, accessStatus);
        
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 200);
        response.put("body", gson.toJson(journalAccess));
        return response;
    }
}
