package therapyapp.model;

import java.time.LocalDateTime;

public class JournalEntry {
    private String journalId;
    private String clientId;
    private String emotion;
    private int intensity;
    private LocalDateTime timestamp;

    public JournalEntry() {}

    public JournalEntry(String journalId, String clientId, String emotion, int intensity, LocalDateTime timestamp) {
        this.journalId = journalId;
        this.clientId = clientId;
        this.emotion = emotion;
        this.intensity = intensity;
        this.timestamp = timestamp;
    }

    public String getJournalId() {
        return journalId;
    }

    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}