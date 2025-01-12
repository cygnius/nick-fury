package com.example;

import com.example.controller.JournalEntryController;
import com.example.model.JournalEntry;
import com.example.repository.JournalEntryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TherapyJournalingApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JournalEntryRepository journalEntryRepository;

    @InjectMocks
    private JournalEntryController journalEntryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(journalEntryController).build();
    }

    @Test
    void testCreateJournalEntry() throws Exception {
        JournalEntry journalEntry = new JournalEntry("Test Title", "Test Content");

        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(journalEntry);

        mockMvc.perform(post("/api/journal-entry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(journalEntry)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));

        verify(journalEntryRepository, times(1)).save(any(JournalEntry.class));
    }

    @Test
    void testGetJournalEntryById() throws Exception {
        JournalEntry journalEntry = new JournalEntry("Test Title", "Test Content");
        journalEntry.setId(1L);

        when(journalEntryRepository.findById(1L)).thenReturn(Optional.of(journalEntry));

        mockMvc.perform(get("/api/journal-entry/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"));

        verify(journalEntryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllJournalEntries() throws Exception {
        JournalEntry journalEntry1 = new JournalEntry("Title 1", "Content 1");
        JournalEntry journalEntry2 = new JournalEntry("Title 2", "Content 2");

        List<JournalEntry> journalEntries = Arrays.asList(journalEntry1, journalEntry2);

        when(journalEntryRepository.findAll()).thenReturn(journalEntries);

        mockMvc.perform(get("/api/journal-entry"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("[0].title").value("Title 1"))
                .andExpect(jsonPath("[1].title").value("Title 2"));

        verify(journalEntryRepository, times(1)).findAll();
    }

    @Test
    void testUpdateJournalEntry() throws Exception {
        JournalEntry journalEntry = new JournalEntry("Old Title", "Old Content");
        journalEntry.setId(1L);
        JournalEntry updatedJournalEntry = new JournalEntry("Updated Title", "Updated Content");

        when(journalEntryRepository.findById(1L)).thenReturn(Optional.of(journalEntry));
        when(journalEntryRepository.save(any(JournalEntry.class))).thenReturn(updatedJournalEntry);

        mockMvc.perform(put("/api/journal-entry/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedJournalEntry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"));

        verify(journalEntryRepository, times(1)).findById(1L);
        verify(journalEntryRepository, times(1)).save(any(JournalEntry.class));
    }

    @Test
    void testDeleteJournalEntry() throws Exception {
        JournalEntry journalEntry = new JournalEntry("Test Title", "Test Content");
        journalEntry.setId(1L);

        when(journalEntryRepository.findById(1L)).thenReturn(Optional.of(journalEntry));

        mockMvc.perform(delete("/api/journal-entry/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Journal entry deleted successfully"));

        verify(journalEntryRepository, times(1)).findById(1L);
        verify(journalEntryRepository, times(1)).deleteById(1L);
    }
}
