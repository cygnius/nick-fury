package com.example.Effdog_Cygnius_API;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

	@Autowired
    private ClientService clientService;
	
	// Create a new client
    @PostMapping("/create")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client createdClient = clientService.createClient(client);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }
    
    // Get a client by clientId
    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable String clientId) {
        Optional<Client> client = clientService.getClientById(clientId);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Update a client
    @PutMapping("/update/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable String clientId, @RequestBody Client updatedClient) {
        Client client = clientService.updateClient(clientId, updatedClient);
        return client != null ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();
    }
    
    // Delete a client
    @DeleteMapping("/delete/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable String clientId) {
        return clientService.deleteClient(clientId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    // Get all clients
    @GetMapping("/getAll")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
}
