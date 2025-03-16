package therapyapp.handler.appointments;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import therapyapp.model.Appointment; 
import therapyapp.util.ResponseHelper;

public class AppointmentHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        String httpMethod = request.getHttpMethod();
        String resource = request.getResource();

        switch (httpMethod) {
            case "POST":
                if ("/appointments".equals(resource)) {
                    return handleCreateAppointment(request);
                }
                break;
            case "GET":
                if (resource.matches("/appointments/[^/]+/status")) {
                    return handleGetAppointmentStatus(request);
                }
                break;
            default:
                return ResponseHelper.createResponse(405, "Method Not Allowed", "");
        }
        return ResponseHelper.createResponse(404, "Resource Not Found", "");
    }

    private APIGatewayProxyResponseEvent handleCreateAppointment(APIGatewayProxyRequestEvent request) {
        try {
            Appointment appointment = objectMapper.readValue(request.getBody(), Appointment.class);
            // Placeholder: Add logic to save appointment to DynamoDB
            Map<String, String> response = new HashMap<>();
            response.put("message", "Appointment request created.");
            return ResponseHelper.createResponse(201, "Created", objectMapper.writeValueAsString(response));
        } catch (IOException e) {
            return ResponseHelper.createResponse(400, "Invalid input", e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent handleGetAppointmentStatus(APIGatewayProxyRequestEvent request) {
        try {
            String appointmentId = request.getPathParameters().get("appointmentId");
            // Placeholder: Retrieve appointment status from DynamoDB using appointmentId
            Map<String, String> response = new HashMap<>();
            response.put("appointmentId", appointmentId);
            response.put("status", "PENDING"); // Replace with actual status from database
            return ResponseHelper.buildResponse(200, "OK", objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            return ResponseHelper.createResponse(404, "Appointment not found", e.getMessage());
        }
    }
}
