package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;

public class FamiliarCardViewModelMapper {

    public FamiliarCardViewModel map(Character familiar) {
        String race = familiar.getRace() != null ? familiar.getRace() : "";
        String characterClass = familiar.getCharacterClass() != null ? familiar.getCharacterClass() : "";
        String raceClass = (race + " " + characterClass).trim();
        String hpText = I18n.t("label.familiarsHP") + ": " + familiar.getCurrentHp();
        String acText = I18n.t("label.familiarsAC") + ": " + familiar.getArmor();
        return new FamiliarCardViewModel(familiar.getName(), raceClass, hpText, acText);
    }
}












