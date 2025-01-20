package io.example.therapy.therapy.services.Service;

import java.util.Optional;

import io.example.therapy.therapy.entity.Client;

public interface ClientService {
    Client saveClient(Client client);
    Optional<Client> getClientByEmail(String email);
    void deleteClientByEmail(String email);
    Iterable<Client> getAllClients();
}
