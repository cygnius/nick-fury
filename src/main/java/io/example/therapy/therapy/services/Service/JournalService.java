package io.example.therapy.therapy.services.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import io.example.therapy.therapy.entity.Journal;

public interface JournalService {


    Journal saveJournal(Journal journal) throws AccessDeniedException;
    Optional<Journal> getJournalByUUID(String journalUuid) throws AccessDeniedException;
    void deleteById(String Uuid) throws AccessDeniedException;
    //Iterable<Journal> getAllJournalsByClientEmail(String clientEmail);
}
