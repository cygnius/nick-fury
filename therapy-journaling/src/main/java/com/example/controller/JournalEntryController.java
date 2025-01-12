package com.example.controller;

import com.example.model.JournalEntry;
import com.example.service.JournalEntryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Journal Entry API")
@RestController
@RequestMapping("/api/journal-entry")
public class JournalEntryController {

	@Autowired
	private JournalEntryService journalEntryService;

	@ApiOperation(value = "Create a new journal entry", response = JournalEntry.class)
	@PostMapping
	public JournalEntry createJournalEntry(
			@ApiParam(value = "Journal entry to be created", required = true) @RequestBody JournalEntry journalEntry) {
		return journalEntryService.createJournalEntry(journalEntry);
	}

	@ApiOperation(value = "Get a journal entry by ID", response = JournalEntry.class)
	@GetMapping("/{id}")
	public JournalEntry getJournalEntryById(
			@ApiParam(value = "ID of the journal entry to retrieve", required = true) @PathVariable("id") Long id) {
		return journalEntryService.getJournalEntryById(id);
	}

	@ApiOperation(value = "Get all journal entries", response = List.class)
	@GetMapping
	public List<JournalEntry> getAllJournalEntries() {
		return journalEntryService.getAllJournalEntries();
	}

	@ApiOperation(value = "Update an existing journal entry", response = JournalEntry.class)
	@PutMapping("/{id}")
	public JournalEntry updateJournalEntry(
			@ApiParam(value = "ID of the journal entry to update", required = true) @PathVariable("id") Long id,
			@RequestBody JournalEntry journalEntry) {
		return journalEntryService.updateJournalEntry(id, journalEntry);
	}

	@ApiOperation(value = "Delete a journal entry by ID", response = String.class)
	@DeleteMapping("/{id}")
	public String deleteJournalEntry(
			@ApiParam(value = "ID of the journal entry to delete", required = true) @PathVariable("id") Long id) {
		return journalEntryService.deleteJournalEntry(id);
	}
}
