package com.example.dnd_manager.screen.form;

import com.example.dnd_manager.domain.Character;

import java.util.Objects;

public class CharacterCoreFormDataApplier {

    public void apply(Character character, CharacterCoreFormData data) {
        Objects.requireNonNull(character, "character must not be null");
        Objects.requireNonNull(data, "data must not be null");

        character.setName(data.name());
        character.setRace(data.race());
        character.setCharacterClass(data.characterClass());
        character.setCurrentHp(data.hp());
        character.setMaxHp(data.hp());
        character.setArmor(data.armor());
        character.setMaxMana(data.mana());
        character.setCurrentMana(data.mana());
        character.setLevel(data.level());
        character.setAvatarImage(data.avatarImage());
        character.setDescription(data.description());
        character.setPersonality(data.personality());
        character.setBackstory(data.backstory());
    }
}













