package io.example.therapy.therapy.services.Impl;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import io.example.therapy.therapy.entity.Therapist;
import io.example.therapy.therapy.repo.TherapistRepo;
import io.example.therapy.therapy.services.Service.TherapistService;

@Service
public class TherapistServiceImpl implements TherapistService {

    @Autowired
    private TherapistRepo therapistRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private final DynamoDBMapper dynamoDBMapper;

    public TherapistServiceImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    public Therapist saveTherapist(Therapist therapist) {
        String hashedPassword = encoder.encode(therapist.getPassword());
        therapist.setPassword(hashedPassword);
    
        return therapistRepo.save(therapist);
    }
    

    public Optional<Therapist> getTherapistByEmail(String email) {
        return therapistRepo.findById(email);
    }

    public Therapist getTherapistByEmailforPublic(String email) {
        Therapist therapist = therapistRepo.findById(email).get();
        therapist.setClients(null);
        therapist.setPassword(null);
        return therapist;
    }

    @Override
    public void deleteTherapistByEmail(String email) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!email.equals(currentUser)) {

            throw new AccessDeniedException("You are not authorized to send this message");
        }
        therapistRepo.deleteById(email);
    }

    //@Override
    // 
    
    public List<Therapist> getAllTherapists() {
    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withLimit(10); // Fetch 10 items at a time

    List<Therapist> therapists =  dynamoDBMapper.scan(Therapist.class, scanExpression);
    for (Therapist therapist : therapists) {
        therapist.setClients(null);
        therapist.setPassword(null);
    }
    return therapists;
}

    public Therapist addClient(String therapistEmail, String clientEmail) throws AccessDeniedException {
        Therapist therapist = therapistRepo.findById(therapistEmail)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));
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
        List<String> clients = therapist.getClients();
        if (clients == null) {
            clients = new ArrayList<>();
        }
        if (!clients.contains(clientEmail)) {
            clients.add(clientEmail);
            therapist.setClients(clients);
            therapistRepo.save(therapist);
        }
        return therapist;
    }

    public List<Therapist> findTherapistsByAvailableSlot(LocalDateTime slot) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withLimit(100); // Fetch 10 items at a time

    List<Therapist> therapists =  dynamoDBMapper.scan(Therapist.class, scanExpression);
    for (Therapist therapist : therapists) {
        therapist.setClients(null);
        therapist.setPassword(null);
    }


        return ((List<Therapist>) therapists).stream()
                .filter(therapist -> therapist.getAvailableSlots() != null 
                                    && therapist.getAvailableSlots().contains(slot))
                .collect(Collectors.toList());
    }
    
// }
    public Therapist updateSpecialization(String email, List<String> newSpecializations) throws AccessDeniedException {
        Therapist therapist = therapistRepo.findById(email)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !email.equals(authentication.getName())) {
            throw new AccessDeniedException("You are not authorized to update specialization");
        }

        // Convert List<String> to a comma-separated string
        therapist.setSpecializationList(newSpecializations);

        return therapistRepo.save(therapist);
    }

    // 
    
    public Therapist updateAvailableSlots(String email, List<LocalDateTime> newSlots) throws AccessDeniedException {
        // Retrieve the therapist by email
        Therapist therapist = therapistRepo.findById(email)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));
    
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        // Check if the authenticated user is authorized to update the available slots
        if (authentication == null || !email.equals(authentication.getName())) {
            throw new AccessDeniedException("You are not authorized to update available slots");
        }
    
        // Update the available slots using the setter method that converts List<LocalDateTime> to String
        therapist.setAvailableSlots(newSlots);
    
        // Save the updated therapist information
        return therapistRepo.save(therapist);
    }
    

    public Therapist removeClient(String therapistEmail, String clientEmail) throws AccessDeniedException {
        Therapist therapist = therapistRepo.findById(therapistEmail)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !therapistEmail.equals(authentication.getName())) {
            throw new AccessDeniedException("You are not authorized to remove this client");
        }

        List<String> clients = therapist.getClients();
        if (clients != null && clients.remove(clientEmail)) {
            therapist.setClients(clients);
            therapistRepo.save(therapist);
        }
        return therapist;
    }

    public List<Therapist> findTherapistsBySpecialization(String specialization) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":specialization", new AttributeValue().withS(specialization));
    
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("contains(specialization, :specialization)")
                .withExpressionAttributeValues(eav);
    
        return dynamoDBMapper.scan(Therapist.class, scanExpression);
    }
    

}
