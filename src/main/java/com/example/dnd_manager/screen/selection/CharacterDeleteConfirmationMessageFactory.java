package com.example.dnd_manager.screen.selection;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;

public class CharacterDeleteConfirmationMessageFactory {

    public String title() {
        return I18n.t("selection.deleteConfirm.title");
    }

    public String message(Character character) {
        return I18n.t("selection.deleteConfirm.message").formatted(characterName(character));
    }

    private String characterName(Character character) {
        String name = character.getName();
        return name == null || name.isBlank() ? I18n.t("selection.unknown") : name;
    }
}
