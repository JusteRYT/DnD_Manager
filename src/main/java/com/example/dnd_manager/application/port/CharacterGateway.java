package com.example.dnd_manager.application.port;

import com.example.dnd_manager.domain.Character;

import java.util.List;
import java.util.Optional;

/**
 * Application-level port for character persistence operations.
 */
public interface CharacterGateway {

    void saveCharacter(Character character);

    Optional<Character> loadCharacter(String name);

    List<String> listCharacterNames();

    void deleteCharacter(Character character);
}













