package com.example.dnd_manager.store;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.infrastructure.persistence.CharacterRepository;
import com.example.dnd_manager.infrastructure.persistence.JsonCharacterRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.example.dnd_manager.domain.Character;
import lombok.Getter;

/**
 * Service for managing character storage.
 */
@Getter
public class StorageService implements CharacterGateway {

    private final CharacterRepository repository;

    public StorageService() {
        this(new JsonCharacterRepository());
    }

    public StorageService(CharacterRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
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











