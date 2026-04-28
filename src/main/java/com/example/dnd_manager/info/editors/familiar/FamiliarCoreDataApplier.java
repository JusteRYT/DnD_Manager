package com.example.dnd_manager.info.editors.familiar;

import com.example.dnd_manager.domain.Character;

import java.util.Objects;

public class FamiliarCoreDataApplier {

    public void apply(Character familiar, FamiliarCoreData data) {
        Objects.requireNonNull(familiar, "familiar must not be null");
        Objects.requireNonNull(data, "data must not be null");

        familiar.setName(data.name());
        familiar.setRace(data.race());
        familiar.setCharacterClass(data.characterClass());
        familiar.setMaxHp(parseSafe(data.hpText()));
        familiar.setCurrentHp(parseSafe(data.hpText()));
        familiar.setArmor(parseSafe(data.armorText()));
        familiar.setMaxMana(parseSafe(data.manaText()));
        familiar.setCurrentMana(parseSafe(data.manaText()));
        familiar.setAvatarImage(data.avatarPath());
    }

    int parseSafe(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(value.replaceAll("\\D", ""));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}













