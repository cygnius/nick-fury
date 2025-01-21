package io.example.therapy.therapy.services.Impl;

import java.nio.file.AccessDeniedException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import io.example.therapy.therapy.entity.Session;
import io.example.therapy.therapy.repo.SessionRepo;
import io.example.therapy.therapy.services.Service.SessionService;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepo sRepo;

    @Autowired
    private final DynamoDBMapper dynamoDBMapper;

    public SessionServiceImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public Session createSession(Session session) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!session.getTherapistEmail().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to send this message");
        }
        // Mark session as open if clientEmail is not provided
        session.setIsOpen(session.getClientEmail() == null);
        return sRepo.save(session);
    }

    public List<Session> findOpenSessions() {
        // Query for sessions that are open to any client
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":isOpen", new AttributeValue().withN("1"));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("isOpen = :isOpen")
                .withExpressionAttributeValues(expressionAttributeValues);

        return dynamoDBMapper.scan(Session.class, scanExpression);
    }

    public List<Session> findSessionByClientEmail(String clientEmail) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!clientEmail.equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to send this message");
        }

        Session session = new Session();
        session.setClientEmail(clientEmail);

        DynamoDBQueryExpression<Session> queryExpression = new DynamoDBQueryExpression<Session>()
                .withIndexName("ClientEmailIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("clientEmail = :clientEmail")
                .withExpressionAttributeValues(Map.of(":clientEmail", new AttributeValue().withS(clientEmail)));

        return dynamoDBMapper.query(Session.class, queryExpression);
    }

    public List<Session> findSessionByTherapistEmail(String therapistEmail) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!therapistEmail.equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to send this message");
        }

        Session session = new Session();
        session.setTherapistEmail(therapistEmail);

        DynamoDBQueryExpression<Session> queryExpression = new DynamoDBQueryExpression<Session>()
                .withIndexName("TherapistEmailIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("therapistEmail = :therapistEmail")
                .withExpressionAttributeValues(Map.of(":therapistEmail", new AttributeValue().withS(therapistEmail)));

        return dynamoDBMapper.query(Session.class, queryExpression);
    }

    public List<Session> findSessionByClientAndTherapist(String clientEmail, String therapistEmail) throws AccessDeniedException {
        if (clientEmail == null || clientEmail.isEmpty() || therapistEmail == null || therapistEmail.isEmpty()) {
            throw new IllegalArgumentException("ClientEmail and TherapistEmail must not be null or empty");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!clientEmail.equals(currentUser) && !therapistEmail.equals(currentUser)) {

            throw new AccessDeniedException("You are not authorized to send this message");
        }

        DynamoDBQueryExpression<Session> queryExpression = new DynamoDBQueryExpression<Session>()
                .withIndexName("ClientEmailIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("clientEmail = :clientEmail")
                .withFilterExpression("therapistEmail = :therapistEmail")
                .withExpressionAttributeValues(Map.of(
                        ":clientEmail", new AttributeValue().withS(clientEmail),
                        ":therapistEmail", new AttributeValue().withS(therapistEmail)
                ));

        List<Session> sessions = dynamoDBMapper.query(Session.class, queryExpression);
        return sessions.stream()
                .sorted(Comparator.comparing(Session::getSessionDate))
                .collect(Collectors.toList());
    }

    public Session addClientToOpenSession(String sessionId, String clientEmail) throws AccessDeniedException {
        Session session = sRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        if (!session.getIsOpen()) {
            throw new RuntimeException("Session is not open now,client cant be added");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();
        //System.out.println("currrent user : "+currentUser);

        // Validate sender ID
        if (!currentUser.equals(clientEmail)&&!currentUser.equals(session.getTherapistEmail())) {

            throw new AccessDeniedException("You are not authorized to send this message");
        }

        if (!session.getIsOpen()) {
            throw new IllegalStateException("Session is not open to all clients");
        }

        session.setClientEmail(clientEmail);
        session.setIsOpen(false); // Mark the session as no longer open
        return sRepo.save(session);
    }

    public String getSharedNotes(String sessionId, String clientEmail) throws AccessDeniedException {
        Session session = sRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();
        System.out.println("current user : "+currentUser);
        System.out.println("therapist mail :"+session.getTherapistEmail());
        System.out.println(!clientEmail.equals(currentUser)&&!currentUser.equals(session.getTherapistEmail()));
        // Validate sender ID
        if (!clientEmail.equals(currentUser)&&!currentUser.equals(session.getTherapistEmail())) {

            throw new AccessDeniedException("You are not authorized to send this message");
        }
        if (!clientEmail.equals(session.getClientEmail())&&!currentUser.equals(session.getTherapistEmail())) {
            throw new AccessDeniedException("You are not authorized to view this session's notes");
        }

        return session.getSharedNotes();
    }

    public String getPrivateNotes(String sessionId, String therapistEmail) throws AccessDeniedException {
        Session session = sRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!therapistEmail.equals(currentUser)) {

            throw new AccessDeniedException("You are not authorized to send this message");
        }

        if (!therapistEmail.equals(session.getTherapistEmail())) {
            throw new AccessDeniedException("You are not authorized to view this session's private notes");
        }

        return session.getPrivateNotes();
    }

    public List<Session> searchSharedNotes(String clientEmail, String searchText) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!clientEmail.equals(currentUser)) {

            throw new AccessDeniedException("You are not authorized to send this message");
        }

        List<Session> clientSessions = findSessionByClientEmail(clientEmail);

        return clientSessions.stream()
                .filter(session -> session.getSharedNotes() != null && session.getSharedNotes().contains(searchText))
                .collect(Collectors.toList());
    }

    public List<Session> searchNotesForTherapist(String therapistEmail, String searchText) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!therapistEmail.equals(currentUser)) {

            throw new AccessDeniedException("You are not authorized to send this message");
        }

        List<Session> therapistSessions = findSessionByTherapistEmail(therapistEmail);

        return therapistSessions.stream()
                .filter(session -> (session.getSharedNotes() != null && session.getSharedNotes().contains(searchText))
                || (session.getPrivateNotes() != null && session.getPrivateNotes().contains(searchText)))
                .collect(Collectors.toList());
    }

}
