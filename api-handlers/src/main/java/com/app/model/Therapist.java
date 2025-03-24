package com.app.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class Therapist {
    private String therapistId;
    private String therapistName;
    private String therapistEmail;
    private String therapistPassword;
    private String specialization;

    public Therapist() {}

    public Therapist(String therapistId, String therapistName, String therapistEmail, String therapistPassword, String specialization) {
        this.therapistId = therapistId;
        this.therapistName = therapistName;
        this.therapistEmail = therapistEmail;
        this.therapistPassword = therapistPassword;
        this.specialization = specialization;
    }

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = {"SpecializationIndex"})
    public String getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(String therapistId) {
        this.therapistId = therapistId;
    }

    @DynamoDbAttribute("therapistName")
    public String getTherapistName() {
        return therapistName;
    }

    public void setTherapistName(String therapistName) {
        this.therapistName = therapistName;
    }

    @DynamoDbAttribute("therapistEmail")
    public String getTherapistEmail() {
        return therapistEmail;
    }

    public void setTherapistEmail(String therapistEmail) {
        this.therapistEmail = therapistEmail;
    }

    @DynamoDbAttribute("therapistPassword")
    public String getTherapistPassword() {
        return therapistPassword;
    }

    public void setTherapistPassword(String therapistPassword) {
        this.therapistPassword = therapistPassword;
    }

    @DynamoDbAttribute("specialization")
    @DynamoDbSecondaryPartitionKey(indexNames = {"SpecializationIndex"})
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
