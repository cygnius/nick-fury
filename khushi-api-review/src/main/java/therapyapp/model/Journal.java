package therapyapp.model;

import java.time.LocalDateTime;

public class Journal {

    private String journalId;
    private String clientId;
    private String emotion;
    private int intensity;
    private String notes;
    private LocalDateTime timestamp;

    public Journal() {}

    public Journal(String journalId, String clientId, String emotion, int intensity, String notes, LocalDateTime timestamp) {
        this.journalId = journalId;
        this.clientId = clientId;
        this.emotion = emotion;
        this.intensity = intensity;
        this.notes = notes;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
