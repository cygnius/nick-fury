package com.myorg.repository;

import com.myorg.models.Message;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.regions.Region;

import java.time.Instant;
import java.util.*;

public class MessageRepository {
    private final DynamoDbClient db = DynamoDbClient.builder()
            .region(Region.of(System.getenv("AWS_REGION")))
            .build();
    private final String tableName = System.getenv("MESSAGE_TABLE");

    public void saveMessage(Message message) {
        String id = UUID.randomUUID().toString();
        String timestamp = Instant.now().toString();

        String senderReceiverKey = message.getSender_id() + "#" + message.getRecipient_id() + "#" + timestamp;
        String receiverSenderKey = message.getRecipient_id() + "#" + message.getSender_id() + "#" + timestamp;

        message.setId(id);
        message.setTimestamp(timestamp);
        message.setSender_receiver_sent_at(senderReceiverKey);
        message.setReceiver_sender_sent_at(receiverSenderKey);

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(message.getId()));
        item.put("sender_id", AttributeValue.fromS(message.getSender_id()));
        item.put("recipient_id", AttributeValue.fromS(message.getRecipient_id()));
        item.put("content", AttributeValue.fromS(message.getContent()));
        item.put("timestamp", AttributeValue.fromS(message.getTimestamp()));
        item.put("sender_receiver_sent_at", AttributeValue.fromS(senderReceiverKey));
        item.put("receiver_sender_sent_at", AttributeValue.fromS(receiverSenderKey));

        PutItemRequest request = PutItemRequest.builder()
            .tableName(tableName)
            .item(item)
            .build();

        db.putItem(request);
    }

    public List<Message> getMessageHistory(String senderId, String recipientId) {
        String gsiName = "sender_receiver_index";
        String partitionKey = senderId;
        String sortKeyPrefix = senderId + "#" + recipientId + "#";

        QueryRequest request = QueryRequest.builder()
            .tableName(tableName)
            .indexName(gsiName)
            .keyConditionExpression("sender_id = :s and begins_with(sender_receiver_sent_at, :prefix)")
            .expressionAttributeValues(Map.of(
                ":s", AttributeValue.fromS(partitionKey),
                ":prefix", AttributeValue.fromS(sortKeyPrefix)
            ))
            .build();

        List<Message> messages = new ArrayList<>();
        QueryResponse response = db.query(request);

        for (Map<String, AttributeValue> item : response.items()) {
            Message msg = new Message();
            msg.setId(item.get("id").s());
            msg.setSender_id(item.get("sender_id").s());
            msg.setRecipient_id(item.get("recipient_id").s());
            msg.setContent(item.get("content").s());
            msg.setTimestamp(item.get("timestamp").s());
            messages.add(msg);
        }

        return messages;
    }
}

























