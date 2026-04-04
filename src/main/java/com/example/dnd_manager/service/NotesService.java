package com.example.dnd_manager.service;

import com.example.dnd_manager.repository.CharacterStoragePathResolver;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Service for handling character-specific text notes.
 * Integrated with the global CharacterStoragePathResolver.
 */
@Slf4j
public class NotesService {

    private static final String NOTES_FILE = "notes.txt";

    public String loadNotes(String characterName) {
        Path path = CharacterStoragePathResolver.getCharacterDir(characterName).resolve(NOTES_FILE);

        if (!Files.exists(path)) return "";

        try {
            return Files.readString(path);
        } catch (IOException e) {
            log.error("Failed to load notes from: {}", path, e);
            return "";
        }
    }

    public void saveNotes(String characterName, String content) {
        Path charDir = CharacterStoragePathResolver.getCharacterDir(characterName);
        Path filePath = charDir.resolve(NOTES_FILE);

        try {
            // Создаем папку персонажа, если её ещё нет (например, новый персонаж)
            if (!Files.exists(charDir)) {
                Files.createDirectories(charDir);
            }
            Files.writeString(filePath, content);
        } catch (IOException e) {
            log.error("Failed to save notes to: {}", filePath, e);
        }
    }
}