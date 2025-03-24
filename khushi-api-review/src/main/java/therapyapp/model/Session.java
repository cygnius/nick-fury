package therapyapp.model;

import java.time.LocalDateTime;

public class Session {

    private String sessionId;
    private String therapistId;
    private String clientId;
    private LocalDateTime sessionDate;
    private String privateNotes;
    private String sharedNotes;
    private String status;


    public Session() {}

    
    public Session(String sessionId, String therapistId, String clientId, String sessionDate, String privateNotes, String sharedNotes) {
        this.sessionId = sessionId;
        this.therapistId = therapistId;
        this.clientId = clientId;
        this.sessionDate = LocalDateTime.parse(sessionDate);
        this.privateNotes = privateNotes;
        this.sharedNotes = sharedNotes;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(String therapistId) {
        this.therapistId = therapistId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getPrivateNotes() {
        return privateNotes;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }

    public String getSharedNotes() {
        return sharedNotes;
    }

    public void setSharedNotes(String sharedNotes) {
        this.sharedNotes = sharedNotes;
    
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { 
        this.status = status;
    }
}
