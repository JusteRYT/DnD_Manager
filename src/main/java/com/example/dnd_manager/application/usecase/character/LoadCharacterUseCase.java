package com.example.dnd_manager.application.usecase.character;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.domain.Character;

import java.util.Objects;
import java.util.Optional;

/**
 * Loads a character by name.
 */
public class LoadCharacterUseCase {

    private final CharacterGateway characterGateway;

    public LoadCharacterUseCase(CharacterGateway characterGateway) {
        this.characterGateway = Objects.requireNonNull(characterGateway, "characterGateway must not be null");
    }

    public Optional<Character> execute(String name) {
        return characterGateway.loadCharacter(name);
    }
}

