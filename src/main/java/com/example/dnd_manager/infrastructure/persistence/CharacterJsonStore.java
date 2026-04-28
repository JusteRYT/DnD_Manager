package com.example.dnd_manager.infrastructure.persistence;

import com.example.dnd_manager.domain.Character;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Low-level JSON storage operations for character entities.
 */
class CharacterJsonStore {

    private static final Logger log = LoggerFactory.getLogger(CharacterJsonStore.class);
    private final ObjectMapper mapper;
    private final CharacterPathProvider pathProvider;

    CharacterJsonStore(ObjectMapper mapper, CharacterPathProvider pathProvider) {
        this.mapper = mapper;
        this.pathProvider = pathProvider;
    }

    void write(Path characterDir, String characterName, Character character) throws IOException {
        Path jsonFile = characterDir.resolve(characterName + ".json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile.toFile(), character);
    }

    Optional<Character> read(String characterName) throws IOException {
        Path jsonFile = pathProvider.getCharacterDir(characterName).resolve(characterName + ".json");
        if (!Files.exists(jsonFile)) {
            return Optional.empty();
        }
        return Optional.of(mapper.readValue(jsonFile.toFile(), Character.class));
    }

    List<String> list(Path root) throws IOException {
        if (!Files.exists(root)) {
            return Collections.emptyList();
        }
        try (var paths = Files.list(root)) {
            return paths
                    .filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    void deleteCharacterDirectory(Path characterDir) throws IOException {
        if (!Files.exists(characterDir)) {
            return;
        }
        try (var paths = Files.walk(characterDir)) {
            paths
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            log.error("Failed to delete path: {}", path, e);
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}












