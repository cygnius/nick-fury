package io.example.therapy.therapy.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import io.example.therapy.therapy.entity.Client;
import io.example.therapy.therapy.entity.Therapist;
import io.example.therapy.therapy.repo.ClientRepo;
import io.example.therapy.therapy.repo.TherapistRepo;

public class ClientTherapistServiceImpl {

    @Autowired
    private ClientRepo clientRepository;
    @Autowired
    private TherapistRepo therapistRepository;


    ///If you want to implement this you can , this will dissociate Client and Therapist from each other
    public void removeTherapistFromClient(String clientEmail, String therapistEmail) {
    // Load client and therapist from the database
    Client client = clientRepository.findById(clientEmail)
                                    .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    Therapist therapist = therapistRepository.findById(therapistEmail)
                                              .orElseThrow(() -> new ResourceNotFoundException("Therapist not found"));

    // Remove therapist from client's therapist list
    client.getTherapists().remove(therapistEmail);

    // Remove client from therapist's client list
    therapist.getClients().remove(clientEmail);

    // Save the updated entities
    clientRepository.save(client);
    therapistRepository.save(therapist);
}

}
