package com.example.dnd_manager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotesServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void saveAndLoadNotes_roundTrip() throws Exception {
        NotesService service = new NotesService(name -> tempDir.resolve(name));

        service.saveNotes("Hero", "Some notes");
        String loaded = service.loadNotes("Hero");

        assertEquals("Some notes", loaded);
        assertTrue(Files.exists(tempDir.resolve("Hero").resolve("notes.txt")));
    }

    @Test
    void loadNotes_returnsEmptyWhenFileMissing() {
        NotesService service = new NotesService(name -> tempDir.resolve(name));

        String loaded = service.loadNotes("UnknownHero");

        assertEquals("", loaded);
    }
}
