package com.example.dnd_manager.application.usecase.character;

import com.example.dnd_manager.application.port.CharacterGateway;

import java.util.List;
import java.util.Objects;

/**
 * Returns the list of existing character names.
 */
public class ListCharacterNamesUseCase {

    private final CharacterGateway characterGateway;

    public ListCharacterNamesUseCase(CharacterGateway characterGateway) {
        this.characterGateway = Objects.requireNonNull(characterGateway, "characterGateway must not be null");
    }

    public List<String> execute() {
        return characterGateway.listCharacterNames();
    }
}

