package io.example.therapy.therapy.services.Impl;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import io.example.therapy.therapy.entity.Message;
import io.example.therapy.therapy.repo.MessageRepo;

@Service
public class MessagesServiceImpl {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Message createMessage(Message message) throws AccessDeniedException {
        // Get authentication inside the method where it's needed
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!message.getSenderId().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to send this message");
        }

        messageRepo.save(message);
        return message;
    }

    public List<Message> messagesSentByCurrentUser(String senderId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Ensure the user is either the sender or receiver
        if (!currentUser.equals(senderId)) {
            throw new AccessDeniedException("You are not authorized to view this conversation");
        }

        Message message = new Message();
        message.setSenderId(senderId);

        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withHashKeyValues(message)
                .withConsistentRead(false);  // Set the hash key value (senderId)

        return dynamoDBMapper.query(Message.class, queryExpression);
    }

    public List<Message> messagesReceivedByCurrentUser(String receiverId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Ensure the user is either the sender or receiver
        if (!currentUser.equals(receiverId)) {
            throw new AccessDeniedException("You are not authorized to view this conversation");
        }

        Message message = new Message();
        message.setReceiverId(receiverId);

        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withIndexName("ReceiverIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("receiverId = :receiverId")
                .withExpressionAttributeValues(Map.of(":receiverId", new AttributeValue().withS(receiverId)));

        return dynamoDBMapper.query(Message.class, queryExpression);
    }


    public List<Message> findMessagesBySenderAndReceiver(String senderId, String receiverId) throws AccessDeniedException {
        // Check for null or empty input
        if (senderId == null || senderId.isEmpty() || receiverId == null || receiverId.isEmpty()) {
            throw new IllegalArgumentException("SenderId and ReceiverId must not be null or empty");
        }
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }
    
        String currentUser = authentication.getName();
    
        // Retrieve the currently authenticated user
        // Ensure the user is either the sender or receiver
        if (!currentUser.equals(senderId) && !currentUser.equals(receiverId)) {
            throw new AccessDeniedException("You are not authorized to view these messages");
        }
    
        // Define the query expression
        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withIndexName("ReceiverIndex") // Use the ReceiverIndex GSI
                .withConsistentRead(false) // For GSIs, use eventual consistency
                .withKeyConditionExpression("receiverId = :receiverId") // Query by receiverId (partition key)
                .withFilterExpression("senderId = :senderId") // Apply filter for senderId
                .withExpressionAttributeValues(Map.of(
                        ":receiverId", new AttributeValue().withS(receiverId),
                        ":senderId", new AttributeValue().withS(senderId)
                ));
    
        // Perform the query
        List<Message> messages = dynamoDBMapper.query(Message.class, queryExpression);
        List<Message> newMessages = new ArrayList<>(messages);
        Collections.sort(newMessages, Comparator.comparing(Message::getTimestamp));
    
        return newMessages;
    }
    
    

    public List<Message> findConversationOfCurrentUser(String senderId, String receiverId) throws AccessDeniedException {
        // Check for null or empty input
        if (senderId == null || senderId.isEmpty() || receiverId == null || receiverId.isEmpty()) {
            throw new IllegalArgumentException("SenderId and ReceiverId must not be null or empty");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Retrieve the currently authenticated user
        // Ensure the user is either the sender or receiver
        if (!currentUser.equals(senderId) && !currentUser.equals(receiverId)) {
            throw new AccessDeniedException("You are not authorized to view this conversation");
        }

        // Fetch messages where the current user is the receiver
        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withIndexName("ReceiverIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("receiverId = :receiverId")
                .withFilterExpression("senderId = :senderId")
                .withExpressionAttributeValues(Map.of(
                        ":receiverId", new AttributeValue().withS(receiverId),
                        ":senderId", new AttributeValue().withS(senderId)
                ));
        List<Message> messages = dynamoDBMapper.query(Message.class, queryExpression);

        // Fetch messages where the current user is the sender
        DynamoDBQueryExpression<Message> queryExpression1 = new DynamoDBQueryExpression<Message>()
                .withIndexName("ReceiverIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("receiverId = :receiverId")
                .withFilterExpression("senderId = :senderId")
                .withExpressionAttributeValues(Map.of(
                        ":receiverId", new AttributeValue().withS(senderId),
                        ":senderId", new AttributeValue().withS(receiverId)
                ));
        List<Message> messages2 = dynamoDBMapper.query(Message.class, queryExpression1);

        List<Message> newMessages = new ArrayList<>(messages);
        newMessages.addAll(messages2);
        Collections.sort(newMessages, Comparator.comparing(Message::getTimestamp));

        return newMessages;
    }

    public List<Message> getAllMessagesOfByCurrentUser(String senderId) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Ensure the user is either the sender or receiver
        if (!currentUser.equals(senderId)) {
            throw new AccessDeniedException("You are not authorized to view this conversation");
        }

        Message message = new Message();
        message.setSenderId(senderId);

        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withHashKeyValues(message)
                .withConsistentRead(false);  // Set the hash key value (senderId)

        List<Message> messageSent = dynamoDBMapper.query(Message.class, queryExpression);

        String receiverId = senderId;
        Message message1 = new Message();
        message1.setReceiverId(receiverId);

        DynamoDBQueryExpression<Message> queryExpression1 = new DynamoDBQueryExpression<Message>()
                .withIndexName("ReceiverIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("receiverId = :receiverId")
                .withExpressionAttributeValues(Map.of(":receiverId", new AttributeValue().withS(receiverId)));

        List<Message> receivedMessage = dynamoDBMapper.query(Message.class, queryExpression1);

        List<Message> allMessages = new ArrayList<>(messageSent);
        allMessages.addAll(receivedMessage);

        Collections.sort(allMessages, Comparator.comparing(Message::getTimestamp));

        return allMessages;

    }

    
    public List<Message> searchMessagesByKeyword(String keyword) throws AccessDeniedException {
        // Check if the keyword is valid
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be null or empty");
        }
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }
    
        String currentUser = authentication.getName();
    
        // Create a DynamoDB query expression
        DynamoDBQueryExpression<Message> queryExpression = new DynamoDBQueryExpression<Message>()
                .withConsistentRead(false) // Use eventual consistency
                .withFilterExpression("contains(messageContent, :keyword)") // Use 'contains' to search for keyword
                .withExpressionAttributeValues(Map.of(":keyword", new AttributeValue().withS(keyword)))
                .withIndexName("SenderIndex") // Specify the index (SenderIndex or ReceiverIndex)
                .withKeyConditionExpression("senderId = :senderId") // Use hash key condition (e.g., senderId)
                .withExpressionAttributeValues(Map.of(
                        ":senderId", new AttributeValue().withS(currentUser), // Use the current user as the sender
                        ":keyword", new AttributeValue().withS(keyword)
                ));
    
        // Perform the query
        List<Message> allMessages = dynamoDBMapper.query(Message.class, queryExpression);
    
        // Filter messages by sender or receiver being the current user
        List<Message> filteredMessages = allMessages.stream()
                .filter(msg -> currentUser.equals(msg.getSenderId()) || currentUser.equals(msg.getReceiverId()))
                .collect(Collectors.toList());
    
        // Sort the messages by timestamp
        Collections.sort(filteredMessages, Comparator.comparing(Message::getTimestamp));
    
        return filteredMessages;
    }
    

}
