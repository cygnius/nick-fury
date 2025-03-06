package therapyapp.model;

public class Mapping {
    private String clientId;
    private String therapistId;
    private String therapistName;

    public Mapping() {}

    public Mapping(String clientId, String therapistId, String therapistName) {
        this.clientId = clientId;
        this.therapistId = therapistId;
        this.therapistName = therapistName;
    }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getTherapistId() { return therapistId; }
    public void setTherapistId(String therapistId) { this.therapistId = therapistId; }
    public String getTherapistName() { return therapistName; }
    public void setTherapistName(String therapistName) { this.therapistName = therapistName; }
}
