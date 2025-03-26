package com.app.repository;

import com.app.model.Message;
import com.app.util.DynamoDBUtil;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.time.Instant;
import java.util.*;

public class MessageRepository {

    private final DynamoDbTable<Message> messagesTable;
    private final DynamoDbIndex<Message> conversationIndex;
    private final DynamoDbIndex<Message> senderIndex;

    public MessageRepository() {
        DynamoDbEnhancedClient enhancedClient = DynamoDBUtil.getDynamoDbEnhancedClient();
        this.messagesTable = enhancedClient.table("Messages", TableSchema.fromBean(Message.class));
        this.conversationIndex = messagesTable.index("ConversationIndex");
        this.senderIndex = messagesTable.index("SenderIndex");
    }

    public void sendMessage(String senderId, String receiverId, String messageContent) {
        String messageId = UUID.randomUUID().toString();
        String timestamp = Instant.now().toString();
        String conversationId = senderId + "#" + receiverId;

        Message message = new Message(messageId, timestamp, senderId, receiverId, messageContent, conversationId);
        messagesTable.putItem(message);
    }

    public List<Message> getMessagesBetweenUsers(String senderId, String receiverId) {
        String conversationId1 = senderId + "#" + receiverId;
        String conversationId2 = receiverId + "#" + senderId;

        QueryConditional queryConditional1 = QueryConditional.keyEqualTo(b ->
                b.partitionValue(conversationId1)
        );

        QueryConditional queryConditional2 = QueryConditional.keyEqualTo(b ->
                b.partitionValue(conversationId2)
        );

        QueryEnhancedRequest queryRequest1 = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional1)
                .build();

        QueryEnhancedRequest queryRequest2 = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional2)
                .build();

        List<Message> messages = new ArrayList<>();

        // Query for first conversation
        for (Page<Message> page : conversationIndex.query(queryRequest1)) {
            for (Message message : page.items()) {
                messages.add(message);
            }
        }

        // Query for second conversation
        for (Page<Message> page : conversationIndex.query(queryRequest2)) {
            for (Message message : page.items()) {
                messages.add(message);
            }
        }

        // Optional: Sort messages by timestamp if needed
        Collections.sort(messages, Comparator.comparing(Message::getTimestamp));

        return messages;
    }

    public Message getMessageBySenderAtTime(String senderId, String timestamp) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(b ->
                b.partitionValue(senderId).sortValue(timestamp)
        );

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build();

        // Query the index
        for (Page<Message> page : senderIndex.query(queryRequest)) {
            // Check if the page has any items
            List<Message> pageItems = page.items();
            if (!pageItems.isEmpty()) {
                // Return the first item found
                return pageItems.get(0);
            }
        }

        // Return null if no message is found
        return null;
    }
}
