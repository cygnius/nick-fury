package therapyapp.model;
import java.util.Date;

public class Appointment {
    private String appointmentId;
    private String clientId;
    private String therapistId;
    private Date appointmentDate;
    private String status;  

    
    public Appointment() {
    }

    public Appointment(String appointmentId, String clientId, String therapistId, Date appointmentDate, String status) {
        this.appointmentId = appointmentId;
        this.clientId = clientId;
        this.therapistId = therapistId;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(String therapistId) {
        this.therapistId = therapistId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

