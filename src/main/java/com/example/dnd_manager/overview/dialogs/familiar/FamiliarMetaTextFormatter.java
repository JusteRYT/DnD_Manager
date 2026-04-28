package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;

public class FamiliarMetaTextFormatter {

    public String format(Character familiar) {
        return String.format(
                "%s • %s • %s %s",
                familiar.getRace(),
                familiar.getCharacterClass(),
                I18n.t("label.familiarsLvl"),
                familiar.getLevel()
        );
    }
}













