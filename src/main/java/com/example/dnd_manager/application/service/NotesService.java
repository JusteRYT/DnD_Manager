package com.example.dnd_manager.application.service;

import com.example.dnd_manager.infrastructure.persistence.CharacterStoragePathResolver;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;

/**
 * Service for handling character-specific text notes.
 * Uses injected character directory resolver for path access.
 */
@Slf4j
public class NotesService {

    private static final String NOTES_FILE = "notes.txt";
    private final Function<String, Path> characterDirResolver;

    public NotesService() {
        this(CharacterStoragePathResolver::getCharacterDir);
    }

    NotesService(Function<String, Path> characterDirResolver) {
        this.characterDirResolver = Objects.requireNonNull(characterDirResolver);
    }

    public String loadNotes(String characterName) {
        Path path = characterDirResolver.apply(characterName).resolve(NOTES_FILE);

        if (!Files.exists(path)) return "";

        try {
            return Files.readString(path);
        } catch (IOException e) {
            log.error("Failed to load notes from: {}", path, e);
            return "";
        }
    }

    public void saveNotes(String characterName, String content) {
        Path charDir = characterDirResolver.apply(characterName);
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












