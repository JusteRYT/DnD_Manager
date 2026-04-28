package com.example.dnd_manager.application.usecase.character;

import com.example.dnd_manager.application.port.CharacterGateway;
import com.example.dnd_manager.domain.Character;

import java.util.Objects;

/**
 * Deletes an existing character.
 */
public class DeleteCharacterUseCase {

    private final CharacterGateway characterGateway;

    public DeleteCharacterUseCase(CharacterGateway characterGateway) {
        this.characterGateway = Objects.requireNonNull(characterGateway, "characterGateway must not be null");
    }

    public void execute(Character character) {
        characterGateway.deleteCharacter(character);
    }
}













