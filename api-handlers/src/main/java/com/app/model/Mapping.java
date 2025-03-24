package com.app.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Mapping {

    private String clientId;
    private String therapistId;
    private String journalAccess;

    public Mapping() {
    }

    public Mapping(String clientId, String therapistId, String journalAccess) {
        this.clientId = clientId;
        this.therapistId = therapistId;
        this.journalAccess = journalAccess;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = {"MappingsIndex"})
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = {"MappingsIndex"})
    public String getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(String therapistId) {
        this.therapistId = therapistId;
    }

    @DynamoDbAttribute("journalAccess")
    public String getJournalAccess() {
        return journalAccess;
    }

    public void setJournalAccess(String journalAccess) {
        this.journalAccess = journalAccess;
    }
}
