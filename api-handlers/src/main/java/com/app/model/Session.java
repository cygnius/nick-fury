package com.app.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Session {

    private String sessionId;
    private String sessionDate;
    private String therapistId;
    private String clientId;
    private String sharedNotes;
    private String privateNotes;

    public Session() {}

    public Session(String sessionId, String sessionDate, String therapistId, String clientId, String sharedNotes, String privateNotes) {
        this.sessionId = sessionId;
        this.sessionDate = sessionDate;
        this.therapistId = therapistId;
        this.clientId = clientId;
        this.sharedNotes = sharedNotes;
        this.privateNotes = privateNotes;
    }

    @DynamoDbPartitionKey
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @DynamoDbSortKey
    @DynamoDbSecondarySortKey(indexNames = {"SessionDateIndex"})
    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"SessionDateIndex"})
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

    public String getSharedNotes() {
        return sharedNotes;
    }

    public void setSharedNotes(String sharedNotes) {
        this.sharedNotes = sharedNotes;
    }

    public String getPrivateNotes() {
        return privateNotes;
    }

    public void setPrivateNotes(String privateNotes) {
        this.privateNotes = privateNotes;
    }
}
