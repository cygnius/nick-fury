package therapyapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import therapyapp.model.Journal;
import therapyapp.repository.JournalRepository;

public class JournalService {

    private final JournalRepository journalRepository;

    @Inject
    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    public Journal createJournal(String clientId, String emotion, int intensity, String notes) {
        String journalId = UUID.randomUUID().toString();
        Journal journal = new Journal(journalId, clientId, emotion, intensity, notes, LocalDateTime.now());
        return journalRepository.saveJournal(journal);
    }

    public Journal getJournal(String journalId) {
        return journalRepository.getJournalById(journalId);
    }

    public List<Journal> getJournalsByClient(String clientId) {
        return journalRepository.getJournalsByClientId(clientId);
    }

    public Journal updateJournal(String journalId, String emotion, int intensity, String notes) {
        Journal journal = journalRepository.getJournalById(journalId);
        if (journal != null) {
            journal.setEmotion(emotion);
            journal.setIntensity(intensity);
            journal.setNotes(notes);
            return journalRepository.updateJournal(journalId, journal);
        }
        throw new IllegalArgumentException("Journal not found");
    }

    public void deleteJournal(String journalId) {
        journalRepository.deleteJournal(journalId);
    }
}
