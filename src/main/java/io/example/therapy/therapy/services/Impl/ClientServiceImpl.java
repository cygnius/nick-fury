package io.example.therapy.therapy.services.Impl;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.example.therapy.therapy.entity.Client;
import io.example.therapy.therapy.entity.Therapist;
import io.example.therapy.therapy.repo.ClientRepo;
import io.example.therapy.therapy.repo.TherapistRepo;
import io.example.therapy.therapy.services.Service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private TherapistRepo therapistRepo;
    @Autowired
    PasswordEncoder encoder;

    @Override
    public Client saveClient(Client client) {
        String hashedPassword = encoder.encode(client.getPassword());
        client.setPassword(hashedPassword);
        return clientRepo.save(client);
    }

    @Override
    public Optional<Client> getClientByEmail(String email) {
        return clientRepo.findById(email);
    }

    public Client getClientByEmailforTherapist(String email) {
        Client client = clientRepo.findById(email).get();
        client.setPassword(null);
        client.setTherapists(null);
        return client;
    }

    @Override
    public void deleteClientByEmail(String email) {
        clientRepo.deleteById(email);
    }

    @Override
    public Iterable<Client> getAllClients() {
        return clientRepo.findAll();
    }

    public Client mapTherapist(String clientEmail, String therapistEmail) throws AccessDeniedException {
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

        Client client = clientRepo.findById(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        List<String> therapists = client.getTherapists();
        if (therapists == null) {
            therapists = new ArrayList<>();
        }
        if (!therapists.contains(therapistEmail)) {
            Therapist therapist = therapistRepo.findById(therapistEmail)
                    .orElseThrow(() -> new RuntimeException("Therapist not found"));

            therapists.add(therapistEmail);
            client.setTherapists(therapists);
            clientRepo.save(client);
            List<String> clients = therapist.getClients();
            if (!clients.contains(clientEmail)) {
                clients.add(currentUser);
            }
            therapist.setClients(clients);
            therapistRepo.save(therapist);
        }
        return client;
    }

    public Client unmapTherapist(String clientEmail, String therapistEmail) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is available
        if (authentication == null) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String currentUser = authentication.getName();

        // Validate sender ID
        if (!clientEmail.equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to perform this operation");
        }

        // Fetch the client and therapist entities
        Client client = clientRepo.findById(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Therapist therapist = therapistRepo.findById(therapistEmail)
                .orElseThrow(() -> new RuntimeException("Therapist not found"));

        // Remove therapist from the client's therapist list
        List<String> therapists = client.getTherapists();
        if (therapists != null && therapists.contains(therapistEmail)) {
            therapists.remove(therapistEmail);
            client.setTherapists(therapists);
            clientRepo.save(client);
        }

        // Remove client from the therapist's client list
        List<String> clients = therapist.getClients();
        if (clients != null && clients.contains(clientEmail)) {
            clients.remove(clientEmail);
            therapist.setClients(clients);
            therapistRepo.save(therapist);
        }

        return client;
    }

}
