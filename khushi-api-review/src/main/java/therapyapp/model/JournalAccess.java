package therapyapp.model;

public class JournalAccess {
    private String therapistId;
    private String clientId;
    private String accessStatus; 

    public JournalAccess() {}

    public JournalAccess(String therapistId, String clientId, String accessStatus) {
        this.therapistId = therapistId;
        this.clientId = clientId;
        this.accessStatus = accessStatus;
    }

    public String getTherapistId() { return therapistId; }
    public void setTherapistId(String therapistId) { this.therapistId = therapistId; }
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getAccessStatus() { return accessStatus; }
    public void setAccessStatus(String accessStatus) { this.accessStatus = accessStatus; }
}
