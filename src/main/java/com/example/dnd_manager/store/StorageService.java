package com.example.dnd_manager.store;

import com.example.dnd_manager.repository.CharacterRepository;
import com.example.dnd_manager.repository.JsonCharacterRepository;

import java.util.List;
import java.util.Optional;
import com.example.dnd_manager.domain.Character;
import lombok.Getter;

/**
 * Service for managing character storage.
 */
@Getter
public class StorageService {

    private CharacterRepository repository;

    public void init() {
        repository = new JsonCharacterRepository();
    }

    public void saveCharacter(Character character) {
        repository.save(character);
    }

    public Optional<Character> loadCharacter(String name) {
        return repository.load(name);
    }

    public List<String> listCharacterNames() {
        return repository.listAll();
    }

}
