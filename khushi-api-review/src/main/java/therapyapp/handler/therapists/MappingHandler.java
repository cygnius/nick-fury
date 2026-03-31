package therapyapp.handler.therapists;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

import therapyapp.model.Mapping;

public class MappingHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    private final Gson gson = new Gson();
    
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        
        Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
        String clientId = pathParameters.get("clientId");
        Map<String, Object> requestBody = gson.fromJson((String) input.get("body"), Map.class);
        String therapistId = (String) requestBody.get("therapistId");
        
        Mapping mapping = new Mapping(clientId, therapistId, "Therapist Name Placeholder");
        
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 201);
        response.put("body", gson.toJson(mapping));
        return response;
    }
}
