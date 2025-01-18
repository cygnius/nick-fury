
package therapyapp.util;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;



public class ResponseHelper {
    public static APIGatewayProxyResponseEvent createResponse(int statusCode, String message, String body) {

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(statusCode);

        response.setBody(body);

        response.setHeaders(Map.of("Content-Type", "application/json"));

        return response;

    }


    public static APIGatewayProxyResponseEvent buildResponse(int statusCode, String message, String body) {

        return createResponse(statusCode, message, body);

    }

}
