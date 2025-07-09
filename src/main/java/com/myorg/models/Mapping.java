package com.myorg.models;

public class Mapping {
    private String id;
    private String clientId;
    private String therapistId;
    private boolean journalAccess;

    public Mapping() {}

    public Mapping(String id, String clientId, String therapistId, boolean journalAccess) {
        this.id = id;
        this.clientId = clientId;
        this.therapistId = therapistId;
        this.journalAccess = journalAccess;
    }

    public String getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getTherapistId() {
        return therapistId;
    }

    public boolean isJournalAccess() {
        return journalAccess;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setTherapistId(String therapistId) {
        this.therapistId = therapistId;
    }

    public void setJournalAccess(boolean journalAccess) {
        this.journalAccess = journalAccess;
    }
}
