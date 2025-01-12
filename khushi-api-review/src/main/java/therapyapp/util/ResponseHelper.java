package therapyapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import java.util.HashMap;
import java.util.Map;


public class ResponseHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Creates a success response with the given status code and body.
     *
     * @param statusCode HTTP status code.
     * @param body Response body.
     * @return A map representing the HTTP response.
     */
    public static Map<String, Object> successResponse(int statusCode, Object body) {
        return buildResponse(statusCode, body);
    }

    /**
     * Creates an error response with the given status code and message.
     *
     * @param statusCode HTTP status code.
     * @param errorMessage Error message to include in the response.
     * @return A map representing the HTTP error response.
     */
    public static Map<String, Object> errorResponse(int statusCode, String errorMessage) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", errorMessage);
        return buildResponse(statusCode, errorBody);
    }

    /**
     * Helper method to construct a generic response.
     *
     * @param statusCode HTTP status code.
     * @param body Response body.
     * @return A map representing the HTTP response.
     */
    private static Map<String, Object> buildResponse(int statusCode, Object body) {
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", statusCode);
        response.put("headers", Map.of("Content-Type", "application/json"));
        try {
            response.put("body", objectMapper.writeValueAsString(body));
        } catch (Exception e) {
            response.put("body", "{\"error\":\"Failed to serialize response\"}");
            response.put("statusCode", HttpStatusCode.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
