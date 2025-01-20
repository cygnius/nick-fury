
package io.example.therapy.therapy.controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.example.therapy.therapy.entity.Session;
import io.example.therapy.therapy.services.Impl.SessionServiceImpl;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionServiceImpl sessionService;

    // Create a session
    @PostMapping
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        try {
            Session createdSession = sessionService.createSession(session);
            return ResponseEntity.ok(createdSession);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(null); // 403 Forbidden if access denied
        }
    }

    // Get open sessions
    @GetMapping("/public/open")
    public ResponseEntity<List<Session>> findOpenSessions() {
        List<Session> openSessions = sessionService.findOpenSessions();
        return ResponseEntity.ok(openSessions);
    }

    // Get sessions by client email
    @GetMapping("/client/{clientEmail}")
    public ResponseEntity<List<Session>> findSessionByClientEmail(@PathVariable String clientEmail) {
        try {
            List<Session> sessions = sessionService.findSessionByClientEmail(clientEmail);
            return ResponseEntity.ok(sessions);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(null); // 403 Forbidden if access denied
        }
    }

    // Get sessions by therapist email
    @GetMapping("/therapist/{therapistEmail}")
    public ResponseEntity<List<Session>> findSessionByTherapistEmail(@PathVariable String therapistEmail) {
        try {
            List<Session> sessions = sessionService.findSessionByTherapistEmail(therapistEmail);
            return ResponseEntity.ok(sessions);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(null); // 403 Forbidden if access denied
        }
    }

    // Get sessions by client and therapist
    @GetMapping("/client/{clientEmail}/therapist/{therapistEmail}")
    public ResponseEntity<List<Session>> findSessionByClientAndTherapist(
            @PathVariable String clientEmail,
            @PathVariable String therapistEmail) {
        try {
            List<Session> sessions = sessionService.findSessionByClientAndTherapist(clientEmail, therapistEmail);
            return ResponseEntity.ok(sessions);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(null); // 403 Forbidden if access denied
        }
    }

    // Add client to open session
    @PostMapping("/add-client/{sessionId}/{clientEmail}")
    public ResponseEntity<Session> addClientToOpenSession(
            @PathVariable String sessionId,
            @PathVariable String clientEmail) {
        try {
            Session updatedSession = sessionService.addClientToOpenSession(sessionId, clientEmail);
            return ResponseEntity.ok(updatedSession);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(null); // 403 Forbidden if access denied
        }
    }

    // Get shared notes for a session
    @GetMapping("/{sessionId}/shared-notes/{clientEmail}")
    public ResponseEntity<String> getSharedNotes(
            @PathVariable String sessionId,
            @PathVariable String clientEmail) {
        try {
            String sharedNotes = sessionService.getSharedNotes(sessionId, clientEmail);
            return ResponseEntity.ok(sharedNotes);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(null); // 403 Forbidden if access denied
        }
    }

    // Get private notes for a session
    @GetMapping("/{sessionId}/private-notes/{therapistEmail}")
    public ResponseEntity<String> getPrivateNotes(
            @PathVariable String sessionId,
            @PathVariable String therapistEmail) {
        try {
            String privateNotes = sessionService.getPrivateNotes(sessionId, therapistEmail);
            return ResponseEntity.ok(privateNotes);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(null); // 403 Forbidden if access denied
        }
    }

    // Search shared notes for a client
    @GetMapping("/search/shared-notes/{clientEmail}/{searchText}")
    public ResponseEntity<List<Session>> searchSharedNotes(
            @PathVariable String clientEmail,
            @PathVariable String searchText) {
        try {
            List<Session> sessions = sessionService.searchSharedNotes(clientEmail, searchText);
            return ResponseEntity.ok(sessions);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(null); // 403 Forbidden if access denied
        }
    }

    // Search notes for a therapist
    @GetMapping("/search/notes/{therapistEmail}/{searchText}")
    public ResponseEntity<List<Session>> searchNotesForTherapist(
            @PathVariable String therapistEmail,
            @PathVariable String searchText) {
        try {
            List<Session> sessions = sessionService.searchNotesForTherapist(therapistEmail, searchText);
            return ResponseEntity.ok(sessions);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(403).body(null); // 403 Forbidden if access denied
        }
    }
}
