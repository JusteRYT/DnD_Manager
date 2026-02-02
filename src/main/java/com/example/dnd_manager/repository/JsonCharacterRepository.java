package com.example.dnd_manager.repository;


import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.example.dnd_manager.domain.Character;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON-based character repository.
 */
public class JsonCharacterRepository implements CharacterRepository {

    private static final String ROOT_DIR = "Character";

    private final ObjectMapper mapper = new ObjectMapper();

    public JsonCharacterRepository() {
        new File(ROOT_DIR).mkdirs();
    }

    @Override
    public void save(Character character) {
        try {
            File file = new File(ROOT_DIR, character.getName() + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, character);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save character", e);
        }
    }

    @Override
    public Optional<Character> load(String name) {
        try {
            File file = new File(ROOT_DIR, name + ".json");
            if (!file.exists()) {
                return Optional.empty();
            }
            return Optional.of(mapper.readValue(file, Character.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load character", e);
        }
    }

    @Override
    public List<String> listAll() {
        try {
            return Files.list(new File(ROOT_DIR).toPath())
                    .map(p -> p.getFileName().toString().replace(".json", ""))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to list characters", e);
        }
    }
}

