package com.example.Effdog_Cygnius_API;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
	
	@Autowired
    private ClientRepository clientRepository;
	
	// Create a new client
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

   // Get a client by clientId
    public Optional<Client> getClientById(String clientId) {
    	
        return clientRepository.findById(clientId);
    }
    
    // Update a client
    public Client updateClient(String clientId, Client updatedClient) {
        if (clientRepository.existsById(clientId)) {
            updatedClient.setClientId(clientId);
            return clientRepository.save(updatedClient);
        }
        return null;
    }
    
    // Delete a client
    public boolean deleteClient(String clientId) {
        if (clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
            return true;
        }
        return false;
    }
    
    // Get all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
