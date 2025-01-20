package io.example.therapy.therapy.controllers;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.example.therapy.therapy.Dtos.EmotionRequest;
import io.example.therapy.therapy.entity.Journal;
import io.example.therapy.therapy.services.Impl.JournalServiceImpl;

@RestController
@RequestMapping("/api/journals")
public class JournalController {

    @Autowired
    private JournalServiceImpl journalService;

    // Create a new journal
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Journal createJournal(@RequestBody Journal journal) throws AccessDeniedException {
        return journalService.saveJournal(journal);
    }

    // Get journal by UUID
    @GetMapping("/{journalUuid}")
    public Optional<Journal> getJournal(@PathVariable String journalUuid) throws AccessDeniedException {
        return journalService.getJournalByUUID(journalUuid);
    }

    // Delete journal by UUID
    @DeleteMapping("/{journalUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJournal(@PathVariable String journalUuid) throws AccessDeniedException {
        journalService.deleteById(journalUuid);
    }

    // Get all journals for a specific client email
    @GetMapping("/client/{clientEmail}")
    public List<Journal> getAllJournalsByClient(@PathVariable String clientEmail) throws AccessDeniedException {
        return journalService.getAllJournalsByClientEmail(clientEmail);
    }

    // Get all journals by title
    @GetMapping("/title/{title}")
    public List<Journal> getAllJournalsByTitle(@PathVariable String title) {
        return journalService.getAllJournalsByTitle(title);
    }

    // Find accessible journals by client email and title
    @GetMapping("/accessible/{clientEmail}/{title}")
    public List<Journal> findAccessibleJournals(@PathVariable String clientEmail, @PathVariable String title) throws AccessDeniedException {
        return journalService.findAccessibleJournalsByClientEmailandTitle(clientEmail, title);
    }

    // Add therapist to a journal
    @PostMapping("/{journalId}/therapist/{therapistEmail}")
    public Journal addTherapistToJournal(@PathVariable String journalId, @PathVariable String therapistEmail) throws AccessDeniedException {
        return journalService.addTherapistToJournal(therapistEmail, journalId);
    }

    // Remove therapist from a journal
    @DeleteMapping("/{journalId}/therapist/{therapistEmail}")
    public Journal removeTherapistFromJournal(@PathVariable String journalId, @PathVariable String therapistEmail) throws AccessDeniedException {
        return journalService.removeTherapistToJournal(therapistEmail, journalId, therapistEmail);
    }

    // Add emotion to a journal
    @PostMapping("/emotion")
    public Journal addEmotionToJournal(@RequestBody EmotionRequest emotionRequest) throws AccessDeniedException {
        return journalService.addEmotionToJournal(emotionRequest);
    }

    // Search journals by keyword (title or content)
    @GetMapping("/search/{keyword}")
    public List<Journal> searchJournals(@PathVariable String keyword) {
        return journalService.searchJournalsByKeyword(keyword);
    }

    // Search accessible journals by keyword (title or content)
    @GetMapping("/search/accessible/{keyword}")
    public List<Journal> searchAccessibleJournals(@PathVariable String keyword) throws AccessDeniedException {
        return journalService.searchAccessibleJournalsByKeyword(keyword);
    }
}
