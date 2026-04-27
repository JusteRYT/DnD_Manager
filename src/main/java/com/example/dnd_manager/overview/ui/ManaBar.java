package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;

/**
 * Panel representing character's mana.
 * Uses safe coloring to avoid ClassCastException warnings in JavaFX 17.
 */
public class ManaBar extends ResourceBar {

    public ManaBar(Character target, Character owner, SaveCharacterUseCase saveCharacterUseCase) {
        super(
                target,
                owner,
                saveCharacterUseCase,
                new CharacterManaResourceMetric(),
                new ResourceValueAdjuster(),
                "manaField.name.overview",
                "#3aa3c3",
                "rgba(58, 163, 195, 0.15)",
                0,
                85.0
        );
    }
}
