package therapyapp.model;

public class Session {
    private String sessionId; 
    private String therapistId;
    private String clientId;
    private String sessionDate; 
    private String privateNotes;
    private String sharedNotes;

    public Session() {}

    public Session(String sessionId, String therapistId, String clientId, String sessionDate, String privateNotes, String sharedNotes) {
        this.sessionId = sessionId;
        this.therapistId = therapistId;
        this.clientId = clientId;
        this.sessionDate = sessionDate;
        this.privateNotes = privateNotes;
        this.sharedNotes = sharedNotes;
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getTherapistId() { return therapistId; }
    public void setTherapistId(String therapistId) { this.therapistId = therapistId; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getSessionDate() { return sessionDate; }
    public void setSessionDate(String sessionDate) { this.sessionDate = sessionDate; }
    public String getPrivateNotes() { return privateNotes; }
    public void setPrivateNotes(String privateNotes) { this.privateNotes = privateNotes; }
    public String getSharedNotes() { return sharedNotes; }
    public void setSharedNotes(String sharedNotes) { this.sharedNotes = sharedNotes; }
}
