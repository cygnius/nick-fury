package io.example.therapy.therapy.entity;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@DynamoDBTable(tableName = "client")
public class Client {

    @DynamoDBHashKey(attributeName = "email")
    @NotNull
    @Email
    private String email; // Email as the primary key

    @DynamoDBAttribute(attributeName = "name")
    @DynamoDBIndexHashKey(globalSecondaryIndexName="NameIndex")
    @NotNull
    private String name;

    @DynamoDBAttribute(attributeName = "password")
    @NotNull
    private String password; // Ensure hashed storage

    @DynamoDBAttribute(attributeName = "description")
    private String description;

    @DynamoDBAttribute(attributeName = "therapists")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.L)
    private List<String> therapists;

    @DynamoDBAttribute(attributeName = "role")
     @DynamoDBTypeConvertedEnum
     @DynamoDBIndexHashKey(globalSecondaryIndexName="RoleIndex")
    private ERole role;
    
    public Client() {
        this.role = ERole.CLIENT; // Initialize in constructor
    }
}
