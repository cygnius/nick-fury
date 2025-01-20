package io.example.therapy.therapy.JwtTest.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.example.therapy.therapy.entity.Client;
import io.example.therapy.therapy.entity.Therapist;
import io.example.therapy.therapy.repo.ClientRepo;
import io.example.therapy.therapy.repo.TherapistRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientRepo clientRepository;

    @Autowired
    private TherapistRepo therapistRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check if the user is a client
        Optional<Client> client = clientRepository.findById(email);
        if (client.isPresent()) {
          //System.out.println("Client password: " + client.get().getPassword());
            return UserDetailsImpl.buildFromClient(client.get());
        }

        // Check if the user is a therapist
        Optional<Therapist> therapist = therapistRepository.findById(email);
        if (therapist.isPresent()) {
          //System.out.println("Therapist password: " + therapist.get().getPassword());
            return UserDetailsImpl.buildFromTherapist(therapist.get());
        }

        // If not found, throw exception
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}