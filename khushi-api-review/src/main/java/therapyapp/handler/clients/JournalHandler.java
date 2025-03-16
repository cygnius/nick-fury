package therapyapp.handler.clients;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import therapyapp.model.Journal;
import therapyapp.service.JournalService;

public class JournalHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Inject
    private JournalService journalService;

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String path = (String) input.get("path");
        String httpMethod = (String) input.get("httpMethod");
        Map<String, Object> response = new HashMap<>();

        try {
            
            Map<String, Object> body = null;
            if (input.get("body") instanceof Map) {
                body = (Map<String, Object>) input.get("body");
            }

            if ("/journals".equals(path) && "POST".equals(httpMethod) && body != null) {
                String clientId = (String) body.get("clientId");
                String emotion = (String) body.get("emotion");
                int intensity = (int) body.get("intensity");
                String notes = (String) body.get("notes");

                Journal journal = journalService.createJournal(clientId, emotion, intensity, notes);
                response.put("statusCode", 201);
                response.put("body", journal);

            } else if (path.startsWith("/journals/") && "GET".equals(httpMethod)) {
                String journalId = path.split("/")[2];
                Journal journal = journalService.getJournal(journalId);
                response.put("statusCode", 200);
                response.put("body", journal);

            } else if ("/journals".equals(path) && "GET".equals(httpMethod)) {
                Map<String, String> queryParams = (Map<String, String>) input.get("queryStringParameters");
                if (queryParams != null) {
                    String clientId = queryParams.get("clientId");
                    response.put("statusCode", 200);
                    response.put("body", journalService.getJournalsByClient(clientId));
                }

            } else if (path.startsWith("/journals/") && "PUT".equals(httpMethod) && body != null) {
                String journalId = path.split("/")[2];
                String emotion = (String) body.get("emotion");
                int intensity = (int) body.get("intensity");
                String notes = (String) body.get("notes");

                Journal updatedJournal = journalService.updateJournal(journalId, emotion, intensity, notes);
                response.put("statusCode", 200);
                response.put("body", updatedJournal);
            }
        } catch (Exception e) {
            response.put("statusCode", 500);
            response.put("body", "Error: " + e.getMessage());
        }

        return response;
    }
}
