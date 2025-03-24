package therapyapp.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import therapyapp.model.Journal;

public class JournalRepository {

    private final Map<String, Journal> journalStore = new HashMap<>();

    @Inject
    public JournalRepository() {}

    public Journal saveJournal(Journal journal) {
        journalStore.put(journal.getJournalId(), journal);
        return journal;
    }

    public Journal getJournalById(String journalId) {
        return journalStore.get(journalId);
    }

    public List<Journal> getJournalsByClientId(String clientId) {
        return journalStore.values().stream()
                .filter(journal -> journal.getClientId().equals(clientId))
                .collect(Collectors.toList());
    }

    public Journal updateJournal(String journalId, Journal updatedJournal) {
        if (journalStore.containsKey(journalId)) {
            journalStore.put(journalId, updatedJournal);
            return updatedJournal;
        }
        throw new IllegalArgumentException("Journal not found");
    }

    public void deleteJournal(String journalId) {
        journalStore.remove(journalId);
    }
}
