package io.example.therapy.therapy.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.example.therapy.therapy.entity.Journal;

@Repository
public interface JournalRepo extends CrudRepository<Journal, String> {
    //List<Journal> findByJournalId(String journalId);
}
