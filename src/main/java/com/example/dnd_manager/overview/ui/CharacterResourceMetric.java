package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;

public interface CharacterResourceMetric {

    int getCurrent(Character character);

    int getMax(Character character);

    void setCurrent(Character character, int value);
}

