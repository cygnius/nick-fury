package io.example.therapy.therapy.services.Impl;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import io.example.therapy.therapy.Dtos.EmotionRequest;
import io.example.therapy.therapy.entity.Emotion;
import io.example.therapy.therapy.entity.Journal;
import io.example.therapy.therapy.repo.JournalRepo;
import io.example.therapy.therapy.services.Service.JournalService;

@Service
public class JournalServiceImpl implements JournalService {

    @Autowired
    private JournalRepo journalRepo;

    @Autowired
    private final DynamoDBMapper dynamoDBMapper;

    public JournalServiceImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public Journal saveJournal(Journal journal) throws AccessDeniedException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!journal.getClientEmail().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to send this message");
        }

        return journalRepo.save(journal);
    }

    @Override
    public Optional<Journal> getJournalByUUID(String journalUuid) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }
        Optional<Journal> journal = journalRepo.findById(journalUuid);
        Journal newJournal = journal.get();
        String currentUser = authentication.getName();

        // Validate sender ID
        if (!newJournal.getClientEmail().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to send this message");
        }

        return journal;
    }

    @Override
    public void deleteById(String Uuid) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }
        Optional<Journal> journal = journalRepo.findById(Uuid);
        Journal newJournal = journal.get();
        String currentUser = authentication.getName();

        // Validate sender ID
        if (!newJournal.getClientEmail().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to send this message");
        }

        journalRepo.deleteById(Uuid);
    }

    public List<Journal> getAllJournalsByClientEmail(String clientEmail) throws AccessDeniedException {
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

        Journal journal = new Journal();
        journal.setClientEmail(clientEmail);

        DynamoDBQueryExpression<Journal> queryExpression = new DynamoDBQueryExpression<Journal>()
                .withIndexName("ClientEmailIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("clientEmail = :clientEmail")
                .withExpressionAttributeValues(Map.of(":clientEmail", new AttributeValue().withS(clientEmail)));

        List<Journal> journals = dynamoDBMapper.query(Journal.class, queryExpression);

        return journals;

    }

    public List<Journal> getAllJournalsByTitle(String title) {
        
        Journal journal = new Journal();
        journal.setTitle(title);

        DynamoDBQueryExpression<Journal> queryExpression = new DynamoDBQueryExpression<Journal>()
                .withIndexName("TitleIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("title = :title")
                .withExpressionAttributeValues(Map.of(":title", new AttributeValue().withS(title)));

        List<Journal> journals = dynamoDBMapper.query(Journal.class, queryExpression);

        return journals;

    }

    public List<Journal> findAccessibleJournalsByClientEmailandTitle(String clientEmail, String title) throws AccessDeniedException {
        if (clientEmail == null || clientEmail.isEmpty() || title == null || title.isEmpty()) {
            throw new IllegalArgumentException("ClientEmail and Title must not be null or empty");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        // Define the query expression
        DynamoDBQueryExpression<Journal> queryExpression = new DynamoDBQueryExpression<Journal>()
                .withIndexName("ClientEmailIndex") // Use the ReceiverIndex GSI
                .withConsistentRead(false) // For GSIs, use eventual consistency
                .withKeyConditionExpression("clientEmail = :clientEmail") // GSI partition key
                .withFilterExpression("title = :title") // Additional filter for senderId
                .withExpressionAttributeValues(Map.of(
                        ":clientEmail", new AttributeValue().withS(clientEmail),
                        ":title", new AttributeValue().withS(title)
                ));

        // Perform the query
        List<Journal> journals = dynamoDBMapper.query(Journal.class, queryExpression);
        if (clientEmail.equals(currentUser)) {
            return journals;
        }

        List<Journal> accessibleJournals = new ArrayList<>();
        for (int i = 0; i < journals.size(); i++) {
            Journal journal = journals.get(i);
            if (journal.getTherapists().contains(currentUser)) {
                accessibleJournals.add(journal);
            }
        }

        if (accessibleJournals.isEmpty()) {
            throw new AccessDeniedException("You are not authorized to send this message");
        }
        // Return the result
        return accessibleJournals;
    }

    public Journal addTherapistToJournal(String therapistEmail, String JournalId) throws AccessDeniedException {

        Journal newJournal = journalRepo.findById(JournalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!newJournal.getClientEmail().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to send this message");
        }

        //Optional<Journal> journal = journalRepo.findById(JournalId);
        //Journal newJournal = journal.get();
        List<String> therapists = newJournal.getTherapists();
        if (therapists == null) {
            therapists = new ArrayList<>();
        }
        if (therapists.contains(therapistEmail)) {
            return newJournal;
        }
        therapists.add(therapistEmail);
        newJournal.setTherapists(therapists);
        journalRepo.save(newJournal);

        return newJournal;

    }

    public Journal removeTherapistToJournal(String therapistEmail, String JournalId, String ClientEmail) throws AccessDeniedException {

        Journal newJournal = journalRepo.findById(JournalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();
        List<String> therapists = newJournal.getTherapists();
        boolean isTrue = false;
        if (therapists.contains(currentUser) && currentUser.equals(therapistEmail)) {
            isTrue = true;
        }

        // Validate sender ID
        if (!newJournal.getClientEmail().equals(currentUser)&&!isTrue) {
            throw new AccessDeniedException("You are not authorized to do this entry");
        }
        //Optional<Journal> journal = journalRepo.findById(JournalId);
        //Journal newJournal = journal.get();

        if (therapists.contains(therapistEmail)) {
            therapists.remove(therapistEmail);
            newJournal.setTherapists(therapists);
            journalRepo.save(newJournal);

        }

        return newJournal;

    }


    public Journal addEmotionToJournal(EmotionRequest emoReq) throws AccessDeniedException {
        // Log incoming request
        System.out.println("Received EmotionRequest: " + emoReq);
    
        // Validate journalId
        String journalId = emoReq.getJournalId();
        if (journalId == null || journalId.isEmpty()) {
            throw new RuntimeException("Journal ID cannot be null or empty");
        }
    
        // Validate emotion
        Emotion emo = emoReq.getEmotion();
        if (emo == null) {
            throw new RuntimeException("Emotion cannot be null");
        }
    
        // Find journal
        Journal newJournal = journalRepo.findById(journalId)
                .orElseThrow(() -> new RuntimeException("Journal not found"));
    
        // Check authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }
    
        String currentUser = authentication.getName();
        if (!newJournal.getClientEmail().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to modify this journal");
        }
    
        // Update journal
        List<Emotion> emotions = newJournal.getEmotions();
        if (emotions == null) {
            emotions = new ArrayList<>();
        }
        emotions.add(emo);
        newJournal.setEmotions(emotions);
    
        // Save changes
        return journalRepo.save(newJournal);
    }
    
    

    public List<Journal> searchJournalsByKeyword(String keyword) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Journal> journals = dynamoDBMapper.scan(Journal.class, scanExpression);

    return journals.stream()
            .filter(journal -> journal.getTitle().contains(keyword)
                    || (journal.getContent() != null && journal.getContent().contains(keyword)))
            .collect(Collectors.toList());

    }

    public List<Journal> searchAccessibleJournalsByKeyword(String keyword) throws AccessDeniedException {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be null or empty");
        }
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }
    
        String currentUser = authentication.getName();
    
        // Retrieve all journals for the authenticated user (client or therapist)
        List<Journal> accessibleJournals = new ArrayList<>();
    
        // Fetch journals by clientEmail (journals created by the user)
        DynamoDBQueryExpression<Journal> clientQueryExpression = new DynamoDBQueryExpression<Journal>()
                .withIndexName("ClientEmailIndex") // Use correct GSI name
                .withConsistentRead(false)
                .withKeyConditionExpression("clientEmail = :clientEmail") // Ensure clientEmail is used as hash key
                .withExpressionAttributeValues(Map.of(":clientEmail", new AttributeValue().withS(currentUser)));
    
        // This will fetch the client journals (journals where clientEmail matches)
        List<Journal> clientJournals = dynamoDBMapper.query(Journal.class, clientQueryExpression);
        accessibleJournals.addAll(clientJournals);
    
        // Fetch journals where the user is a therapist
        DynamoDBQueryExpression<Journal> therapistQueryExpression = new DynamoDBQueryExpression<Journal>()
                .withIndexName("TitleIndex") // Assuming you use a GSI for efficient searching by title
                .withConsistentRead(false)
                .withKeyConditionExpression("title = :title") // Ensure title is used as hash key (if TitleIndex uses title as hash key)
                .withFilterExpression("contains(therapists, :therapistEmail)") // Filtering by therapists list
                .withExpressionAttributeValues(Map.of(":therapistEmail", new AttributeValue().withS(currentUser),
                        ":title", new AttributeValue().withS("Some title")));  // Adjust for your actual query condition
    
        // This will fetch the therapist journals (journals where the user is listed in therapists)
        List<Journal> therapistJournals = dynamoDBMapper.query(Journal.class, therapistQueryExpression);
        accessibleJournals.addAll(therapistJournals);
    
        // Filter journals by keyword in title or content
        List<Journal> filteredJournals = accessibleJournals.stream()
                .filter(journal -> (journal.getTitle() != null && journal.getTitle().contains(keyword))
                        || (journal.getContent() != null && journal.getContent().contains(keyword)))
                .collect(Collectors.toList());
    
        return filteredJournals;
    }
    

}
