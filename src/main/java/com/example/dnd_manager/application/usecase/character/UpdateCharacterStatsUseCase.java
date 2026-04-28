package com.example.dnd_manager.application.usecase.character;

import com.example.dnd_manager.domain.Character;

import java.util.Objects;

public class UpdateCharacterStatsUseCase {

    private final SaveCharacterUseCase saveCharacterUseCase;

    public UpdateCharacterStatsUseCase(SaveCharacterUseCase saveCharacterUseCase) {
        this.saveCharacterUseCase = Objects.requireNonNull(saveCharacterUseCase, "saveCharacterUseCase must not be null");
    }

    public void execute(
            Character character,
            Integer maxHp,
            Integer armor,
            Integer maxMana,
            Integer level
    ) {
        if (maxHp != null) {
            character.setMaxHp(maxHp);
        }
        if (armor != null) {
            character.setArmor(armor);
        }
        if (maxMana != null) {
            character.setMaxMana(maxMana);
        }
        if (level != null) {
            character.setLevel(level);
        }
        saveCharacterUseCase.execute(character);
    }
}












