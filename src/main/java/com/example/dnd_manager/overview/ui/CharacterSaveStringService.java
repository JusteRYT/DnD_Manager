package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;

import java.util.Objects;

public class CharacterSaveStringService {

    private final SaveCharacterUseCase saveCharacterUseCase;

    public CharacterSaveStringService(SaveCharacterUseCase saveCharacterUseCase) {
        this.saveCharacterUseCase = Objects.requireNonNull(saveCharacterUseCase, "saveCharacterUseCase must not be null");
    }

    public boolean persist(Character character, String rawText) {
        String next = rawText == null ? "" : rawText.trim();
        String current = character.getSaveString() == null ? "" : character.getSaveString().trim();
        if (current.equals(next)) {
            return false;
        }
        character.setSaveString(next);
        saveCharacterUseCase.execute(character);
        return true;
    }
}
