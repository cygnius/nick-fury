package therapyapp.handler.sessions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import therapyapp.model.Session;
import therapyapp.service.SessionService;

public class SessionHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Inject
    private SessionService sessionService;

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String path = (String) input.get("path");
        String httpMethod = (String) input.get("httpMethod");
        Map<String, Object> response = new HashMap<>();

        try {
            if ("/sessions".equals(path) && "POST".equals(httpMethod)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> body = (Map<String, Object>) input.get("body");
                if (body != null) {
                    Session session = new Session();
                    session.setTherapistId((String) body.get("therapistId"));
                    session.setClientId((String) body.get("clientId"));
                    session.setSessionDate(LocalDateTime.parse((String) body.get("sessionDate")));
                    session.setPrivateNotes((String) body.get("privateNotes"));
                    session.setSharedNotes((String) body.get("sharedNotes"));
                    session.setStatus((String) body.get("status"));

                    Session createdSession = sessionService.createSession(session);
                    response.put("statusCode", 201);
                    response.put("body", createdSession);
                }

            } else if (path.startsWith("/sessions/") && "GET".equals(httpMethod)) {
                String sessionId = path.split("/")[2];
                Session session = sessionService.getSession(sessionId);
                response.put("statusCode", 200);
                response.put("body", session);

            } else if (path.startsWith("/sessions") && "GET".equals(httpMethod)) {
                @SuppressWarnings("unchecked")
                Map<String, String> queryStringParameters = (Map<String, String>) input.get("queryStringParameters");
                if (queryStringParameters != null) {
                    String therapistId = queryStringParameters.get("therapistId");
                    LocalDateTime afterDate = LocalDateTime.parse(queryStringParameters.get("afterDate"));
                    response.put("statusCode", 200);
                    response.put("body", sessionService.listSessionsByTherapist(therapistId, afterDate));
                }
            }
        } catch (Exception e) {
            response.put("statusCode", 500);
            response.put("body", "Error: " + e.getMessage());
        }

        return response;
    }
}
