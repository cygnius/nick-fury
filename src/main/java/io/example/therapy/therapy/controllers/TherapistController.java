package io.example.therapy.therapy.controllers;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.example.therapy.therapy.entity.Therapist;
import io.example.therapy.therapy.services.Impl.TherapistServiceImpl;

@RestController
@RequestMapping("/api/therapists")
public class TherapistController {

    @Autowired
    private TherapistServiceImpl therapistService;

    // Endpoint to save a new therapist
    // @PostMapping("public/save")
    // public ResponseEntity<Therapist> saveTherapist(@RequestBody Therapist therapist) {
    //     return ResponseEntity.ok(therapistService.saveTherapist(therapist));
    // }

    // Endpoint to get a therapist by email
    @GetMapping("/public/{email}")
    public ResponseEntity<Therapist> getTherapistByEmailforPublic(@PathVariable String email) {
        return ResponseEntity.ok(therapistService.getTherapistByEmailforPublic(email));
    }

    // Endpoint to get all therapists
    @GetMapping("/public/all")
    public ResponseEntity<Iterable<Therapist>> getAllTherapists() {
        return ResponseEntity.ok(therapistService.getAllTherapists());
    }

    // Endpoint to delete a therapist by email
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteTherapistByEmail(@PathVariable String email) throws AccessDeniedException {
        therapistService.deleteTherapistByEmail(email);
        return ResponseEntity.noContent().build();
    }

    // Endpoint to add a client to a therapist's list
    @PostMapping("/{therapistEmail}/add-client/{clientEmail}")
    public ResponseEntity<Therapist> addClientToTherapist(@PathVariable String therapistEmail, @PathVariable String clientEmail) throws AccessDeniedException {
        return ResponseEntity.ok(therapistService.addClient(therapistEmail, clientEmail));
    }

    // Endpoint to find therapists by specialization
    @GetMapping("/public/specialization/{specialization}")
    public ResponseEntity<List<Therapist>> findTherapistsBySpecialization(@PathVariable String specialization) {
        return ResponseEntity.ok(therapistService.findTherapistsBySpecialization(specialization));
    }

    // Endpoint to find therapists available at a specific time slot
    @GetMapping("/public/available-slot/{slot}")
    public ResponseEntity<List<Therapist>> findTherapistsByAvailableSlot(@PathVariable String slot) {
        LocalDateTime dateTime = LocalDateTime.parse(slot);
        return ResponseEntity.ok(therapistService.findTherapistsByAvailableSlot(dateTime));
    }

    // Endpoint to update therapist specialization
    @PutMapping("/{email}/update-specialization")
    public ResponseEntity<Therapist> updateSpecialization(@PathVariable String email, @RequestBody List<String> newSpecializations) throws AccessDeniedException {
        return ResponseEntity.ok(therapistService.updateSpecialization(email, newSpecializations));
    }

    // Endpoint to update therapist available slots
    @PutMapping("/{email}/update-slots")
    public ResponseEntity<Therapist> updateAvailableSlots(@PathVariable String email, @RequestBody List<LocalDateTime> newSlots) throws AccessDeniedException {
        return ResponseEntity.ok(therapistService.updateAvailableSlots(email, newSlots));
    }

    // Endpoint to remove a client from a therapist's list
    @DeleteMapping("/{therapistEmail}/remove-client/{clientEmail}")
    public ResponseEntity<Therapist> removeClientFromTherapist(@PathVariable String therapistEmail, @PathVariable String clientEmail) throws AccessDeniedException {
        return ResponseEntity.ok(therapistService.removeClient(therapistEmail, clientEmail));
    }
}
