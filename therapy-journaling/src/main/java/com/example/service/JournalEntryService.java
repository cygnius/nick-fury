package com.example.service;

import com.example.model.JournalEntry;
import com.example.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;

    @Autowired
    public JournalEntryService(JournalEntryRepository journalEntryRepository) {
        this.journalEntryRepository = journalEntryRepository;
    }

    // Get all journal entries
    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll(); // Fetches all entries from the repository
    }

    // Get a single journal entry by ID
    public JournalEntry getJournalEntryById(Long id) {
        Optional<JournalEntry> entry = journalEntryRepository.findById(id); // Finds the entry by ID
        return entry.orElse(null); // Return the entry if found, otherwise return null
    }

    // Create a new journal entry
    public JournalEntry createJournalEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry); // Saves the journal entry to the repository
    }

    // Update an existing journal entry
    public JournalEntry updateJournalEntry(Long id, JournalEntry journalEntry) {
        Optional<JournalEntry> existingEntry = journalEntryRepository.findById(id); // Checks if entry exists
        if (existingEntry.isPresent()) {
            journalEntry.setId(id); // Set the ID to ensure the correct entry is updated
            return journalEntryRepository.save(journalEntry); // Save the updated entry
        }
        return null; // Return null if the entry doesn't exist
    }

    // Delete a journal entry by ID
    public String deleteJournalEntry(Long id) {
        journalEntryRepository.deleteById(id); // Deletes the journal entry by ID
        return "Journal Entry deleted successfully!"; // Return a success message
    }
}
