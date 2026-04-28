package com.example.dnd_manager.application.usecase.character;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.domain.Character;

import java.util.Objects;

/**
 * Saves character changes.
 */
public class SaveCharacterUseCase {

    private final CharacterGateway characterGateway;

    public SaveCharacterUseCase(CharacterGateway characterGateway) {
        this.characterGateway = Objects.requireNonNull(characterGateway, "characterGateway must not be null");
    }

    public void execute(Character character) {
        characterGateway.saveCharacter(character);
    }
}













