package io.example.therapy.therapy.entity;

import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import io.example.therapy.therapy.converter.LocalDateTimeConverter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "message")
public class Message {

    @DynamoDBHashKey(attributeName = "messageKey")
    //@NotNull
    private String messageKey; // Composite key: senderId#receiverId#timestamp

    @DynamoDBAttribute(attributeName = "senderId")
    @NotNull
    @Email
    @DynamoDBIndexHashKey(globalSecondaryIndexName="SenderIndex")
    private String senderId;

    @DynamoDBIndexHashKey(globalSecondaryIndexName="ReceiverIndex")
    @DynamoDBAttribute(attributeName = "receiverId")
    @NotNull
    @Email
    private String receiverId;

    @DynamoDBAttribute(attributeName = "messageContent")
    @NotNull
    private String messageContent;

    @DynamoDBIndexHashKey(globalSecondaryIndexName="TimeStampIndex")
    @NotNull
    @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class)
    private LocalDateTime timestamp;
}
