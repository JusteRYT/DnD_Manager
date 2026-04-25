package com.example.dnd_manager.store;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.repository.CharacterRepository;
import com.example.dnd_manager.repository.JsonCharacterRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.example.dnd_manager.domain.Character;

/**
 * Service for managing character storage.
 */
public class StorageService implements CharacterGateway {

    private final CharacterRepository repository;

    public StorageService() {
        this(new JsonCharacterRepository());
    }

    public StorageService(CharacterRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    public CharacterRepository getRepository() {
        return repository;
    }

    /**
     * Backward-compatible lifecycle hook.
     * Storage now initializes dependencies in constructor.
     */
    @Deprecated
    public void init() {
        // No-op for backward compatibility.
    }

    @Override
    public void saveCharacter(Character character) {
        repository.save(character);
    }

    @Override
    public Optional<Character> loadCharacter(String name) {
        return repository.load(name);
    }

    @Override
    public List<String> listCharacterNames() {
        return repository.listAll();
    }

    @Override
    public void deleteCharacter(Character character) {
        repository.delete(character.getName());
    }

}
