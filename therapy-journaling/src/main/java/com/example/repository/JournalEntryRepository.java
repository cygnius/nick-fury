package com.example.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.JournalEntry;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
}
