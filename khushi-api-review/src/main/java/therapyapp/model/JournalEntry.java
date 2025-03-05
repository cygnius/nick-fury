package therapyapp.model;

public class JournalEntry {
    private String clientId;
    private String timestamp; 
    private String emotion;
    private int intensity;

    public JournalEntry() {}

    public JournalEntry(String clientId, String timestamp, String emotion, int intensity) {
        this.clientId = clientId;
        this.timestamp = timestamp;
        this.emotion = emotion;
        this.intensity = intensity;
    }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }
    public int getIntensity() { return intensity; }
    public void setIntensity(int intensity) { this.intensity = intensity; }
}
