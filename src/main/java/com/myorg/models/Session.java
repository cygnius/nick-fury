package com.myorg.models;

public class Session {
    private String id;
    private String therapistId;
    private String clientId; // Can be null if not booked
    private String title;
    private String scheduledAt; // ISO-8601 format string: yyyy-MM-dd'T'HH:mm:ss
    private int durationMin;
    private String status; // "Requested", "Confirmed", or "Cancelled"
    private boolean isShared;
    private String content; // Optional notes

    public Session() {}

    public Session(String id, String therapistId, String clientId, String title, String scheduledAt,
                   int durationMin, String status, boolean isShared, String content) {
        this.id = id;
        this.therapistId = therapistId;
        this.clientId = clientId;
        this.title = title;
        this.scheduledAt = scheduledAt;
        this.durationMin = durationMin;
        this.status = status;
        this.isShared = isShared;
        this.content = content;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(String scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public int getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(int durationMin) {
        this.durationMin = durationMin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setShared(boolean shared) {
        isShared = shared;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
