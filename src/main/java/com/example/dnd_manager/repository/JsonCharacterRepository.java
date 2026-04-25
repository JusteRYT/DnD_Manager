package com.example.dnd_manager.repository;

import com.example.dnd_manager.domain.Character;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * JSON-based character repository.
 * Stores each character in its own directory with icons.
 */
public class JsonCharacterRepository implements CharacterRepository {

    private static final Logger log = LoggerFactory.getLogger(JsonCharacterRepository.class);
    private static final String ICON_DIR = "icon";

    private final CharacterJsonStore jsonStore;
    private final CharacterAssetProcessor assetProcessor;

    public JsonCharacterRepository() {
        this(new ObjectMapper(), new CharacterAssetProcessor());
    }

    JsonCharacterRepository(ObjectMapper objectMapper, CharacterAssetProcessor assetProcessor) {
        CharacterStoragePathResolver.migrateIfNeeded();
        try {
            CharacterStoragePathResolver.ensureRootExists();
        } catch (IOException e) {
            log.error("Could not initialize storage", e);
            throw new RuntimeException("Could not initialize storage", e);
        }

        this.jsonStore = new CharacterJsonStore(objectMapper);
        this.assetProcessor = assetProcessor;
    }

    private Path getRoot() {
        return CharacterStoragePathResolver.getRoot();
    }

    @Override
    public void save(Character character) {
        log.info("Saving character: {}", character.getName());
        validate(character);

        try {
            String oldName = character.getOriginalName();
            String newName = character.getName();
            Path characterDir = CharacterStoragePathResolver.getCharacterDir(newName);

            if (oldName != null && !oldName.equals(newName)) {
                renameCharacterDirectory(oldName, newName, characterDir);
            }

            Path iconDir = characterDir.resolve(ICON_DIR);
            Files.createDirectories(iconDir);
            assetProcessor.copyIcons(character, iconDir);
            jsonStore.write(characterDir, newName, character);

            character.markSaved();
        } catch (Exception e) {
            log.error("Failed to save character", e);
            throw new RuntimeException("Save failed", e);
        }
    }

    @Override
    public Optional<Character> load(String name) {
        log.debug("Loading character: {}", name);
        try {
            Optional<Character> character = jsonStore.read(name);
            if (character.isEmpty()) {
                Path jsonFile = CharacterStoragePathResolver.getCharacterDir(name).resolve(name + ".json");
                log.warn("Character file not found: {}", jsonFile);
            }
            return character;
        } catch (Exception e) {
            log.error("Failed to load character: {}", name, e);
            throw new RuntimeException("Failed to load character", e);
        }
    }

    @Override
    public List<String> listAll() {
        try {
            return jsonStore.list(getRoot());
        } catch (Exception e) {
            log.error("Failed to list characters", e);
            throw new RuntimeException("Failed to list characters", e);
        }
    }

    @Override
    public void delete(String name) {
        log.info("Deleting character: {}", name);
        Path characterDir = CharacterStoragePathResolver.getCharacterDir(name);
        try {
            jsonStore.deleteCharacterDirectory(characterDir);
        } catch (IOException e) {
            log.error("Failed to delete character directory: {}", name, e);
            throw new RuntimeException("Failed to delete character " + name, e);
        }
    }

    /**
     * Kept for backward compatibility with existing tests/extensions.
     */
    public String extractFileName(String sourcePath) {
        return assetProcessor.extractFileName(sourcePath);
    }

    private void renameCharacterDirectory(String oldName, String newName, Path characterDir) throws IOException {
        Path oldDir = CharacterStoragePathResolver.getCharacterDir(oldName);
        if (Files.exists(oldDir)) {
            log.info("Renaming character directory from {} to {}", oldName, newName);
            Files.move(oldDir, characterDir, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(characterDir.resolve(oldName + ".json"));
        }
    }

    private void validate(Character character) {
        if (character.getName() == null || character.getName().isBlank()) {
            throw new IllegalArgumentException("Character name must not be empty");
        }
    }
}

