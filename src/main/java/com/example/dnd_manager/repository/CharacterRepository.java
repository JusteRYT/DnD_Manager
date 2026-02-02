package com.example.dnd_manager.repository;

import java.util.List;
import java.util.Optional;
import com.example.dnd_manager.domain.Character;

/**
 * Repository for character persistence.
 */
public interface CharacterRepository {

    void save(Character character);

    Optional<Character> load(String name);

    List<String> listAll();
}
