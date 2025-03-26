package com.app.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;


@DynamoDbBean
public class Message {
    private String messageId;
    private String timestamp;
    private String senderId;
    private String receiverId;
    private String messageContent;
    private String conversationId;

    public Message() {
    }

    public Message(String messageId, String timestamp, String senderId, String receiverId, String messageContent, String conversationId) {
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageContent = messageContent;
        this.conversationId = conversationId;
    }

    @DynamoDbPartitionKey
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @DynamoDbSortKey
    @DynamoDbSecondarySortKey(indexNames = {"ConversationIndex", "SenderIndex"})
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"SenderIndex"})
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"ConversationIndex"})
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
