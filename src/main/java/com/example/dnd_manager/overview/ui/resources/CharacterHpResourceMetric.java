package com.example.dnd_manager.overview.ui.resources;

import com.example.dnd_manager.domain.Character;

public class CharacterHpResourceMetric implements CharacterResourceMetric {

    @Override
    public int getCurrent(Character character) {
        return character.getCurrentHp();
    }

    @Override
    public int getMax(Character character) {
        return character.getMaxHp();
    }

    @Override
    public void setCurrent(Character character, int value) {
        character.setCurrentHp(value);
    }
}













