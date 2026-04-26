package com.example.dnd_manager.application.usecase.character;

import com.example.dnd_manager.domain.Character;

import java.util.Objects;

public class LevelUpCharacterUseCase {

    private final SaveCharacterUseCase saveCharacterUseCase;

    public LevelUpCharacterUseCase(SaveCharacterUseCase saveCharacterUseCase) {
        this.saveCharacterUseCase = Objects.requireNonNull(saveCharacterUseCase, "saveCharacterUseCase must not be null");
    }

    public void execute(Character character) {
        character.setLevel(character.getLevel() + 1);
        saveCharacterUseCase.execute(character);
    }
}
