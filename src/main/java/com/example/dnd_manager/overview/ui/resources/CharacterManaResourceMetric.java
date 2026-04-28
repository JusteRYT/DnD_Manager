package com.example.dnd_manager.overview.ui.resources;

import com.example.dnd_manager.domain.Character;

public class CharacterManaResourceMetric implements CharacterResourceMetric {

    @Override
    public int getCurrent(Character character) {
        return character.getCurrentMana();
    }

    @Override
    public int getMax(Character character) {
        return character.getMaxMana();
    }

    @Override
    public void setCurrent(Character character, int value) {
        character.setCurrentMana(value);
    }
}













