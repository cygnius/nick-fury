package therapyapp.model;

import java.util.Date;

public class Session {
    private String sessionId;
    private String clientId;
    private String therapistId;
    private Date sessionDate;
    private String notes;

    // Constructors
    public Session() {
    }

    public Session(String sessionId, String clientId, String therapistId, Date sessionDate, String notes) {
        this.sessionId = sessionId;
        this.clientId = clientId;
        this.therapistId = therapistId;
        this.sessionDate = sessionDate;
        this.notes = notes;
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
