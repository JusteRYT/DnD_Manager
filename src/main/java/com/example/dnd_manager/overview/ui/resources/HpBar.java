package com.example.dnd_manager.overview.ui.resources;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
public class HpBar extends ResourceBar {

    public HpBar(Character target, Character owner, SaveCharacterUseCase saveCharacterUseCase) {
        super(
                target,
                owner,
                saveCharacterUseCase,
                new CharacterHpResourceMetric(),
                new ResourceValueAdjuster(),
                "label.familiarsHP",
                "#ff4444",
                "rgba(255, 68, 68, 0.15)",
                1,
                null
        );
    }
}












