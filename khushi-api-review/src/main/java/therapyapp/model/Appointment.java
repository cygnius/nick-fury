package therapyapp.model;

public class Appointment {
    private String appointmentId; 
    private String clientId;
    private String therapistId;
    private String appointmentDate; 
    private String status; 
    private String createdAt; 

    public Appointment() {}

    public Appointment(String appointmentId, String clientId, String therapistId, String appointmentDate, String status, String createdAt) {
        this.appointmentId = appointmentId;
        this.clientId = clientId;
        this.therapistId = therapistId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getTherapistId() { return therapistId; }
    public void setTherapistId(String therapistId) { this.therapistId = therapistId; }
    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
