package io.example.therapy.therapy.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import io.example.therapy.therapy.converter.LocalDateTimeListConverter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "therapist")
public class Therapist {

    @DynamoDBHashKey(attributeName = "email")
    @NotNull
    @Email
    private String email; // Primary key

    @DynamoDBAttribute(attributeName = "password")
    @NotNull
    private String password; // Store hashed passwords

    @DynamoDBAttribute(attributeName = "name")
    @NotNull
    private String name;
///////////////////////
    @DynamoDBAttribute(attributeName = "specialization")
    //@DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.L)
    @DynamoDBIndexHashKey(globalSecondaryIndexName="SpecializationIndex")
    //private List<String> specialization= new ArrayList<>();
private String specialization; // Store as a comma-separated string

public List<String> getSpecializationList() {
    if (specialization == null || specialization.isEmpty()) {
        return new ArrayList<>();
    }
    return Arrays.asList(specialization.split(","));
}

public void setSpecializationList(List<String> specializationList) {
    this.specialization = String.join(",", specializationList);
}



    /////////////
    @DynamoDBAttribute(attributeName = "role")
    @DynamoDBIndexHashKey(globalSecondaryIndexName="RoleIndex")
    private String role;

    @DynamoDBTypeConverted(converter = LocalDateTimeListConverter.class)
    @DynamoDBAttribute(attributeName = "availableSlots")
    private List<LocalDateTime> availableSlots;



    @DynamoDBAttribute(attributeName = "clients")
    private List<String> clients= new ArrayList<>();

    public Therapist() {
        this.role = "THERAPIST"; // Default to THERAPIST
    }

}
