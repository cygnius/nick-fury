package io.example.therapy.therapy.controllers;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.example.therapy.therapy.entity.Client;
import io.example.therapy.therapy.services.Impl.ClientServiceImpl;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientServiceImpl clientService;

    // Save a new client
    @PostMapping
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        Client savedClient = clientService.saveClient(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    // Get a client by email
    @GetMapping("/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
        Client client = clientService.getClientByEmailforTherapist(email);
        return ResponseEntity.ok(client);
    }

    // Get all clients
    // @GetMapping
    // public ResponseEntity<Iterable<Client>> getAllClients() {
    //     Iterable<Client> clients = clientService.getAllClients();
    //     return new ResponseEntity<>(clients, HttpStatus.OK);
    // }

    // Delete a client by email
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteClientByEmail(@PathVariable String email) {
        clientService.deleteClientByEmail(email);
        return ResponseEntity.noContent().build();
    }

    // Map therapist to client
    @PostMapping("/{clientEmail}/mapTherapist/{therapistEmail}")
    public ResponseEntity<Client> mapTherapist(@PathVariable String clientEmail, @PathVariable String therapistEmail) {
        try {
            Client updatedClient = clientService.mapTherapist(clientEmail, therapistEmail);
            return ResponseEntity.ok(updatedClient);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Unmap therapist from client
    @PostMapping("/{clientEmail}/unmapTherapist/{therapistEmail}")
    public ResponseEntity<Client> unmapTherapist(@PathVariable String clientEmail, @PathVariable String therapistEmail) {
        try {
            Client updatedClient = clientService.unmapTherapist(clientEmail, therapistEmail);
            return ResponseEntity.ok(updatedClient);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
