package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;

public class CurrencyService {

    public void adjust(Character character, int delta) {
        character.setTotalCooper(Math.max(0, character.getTotalCooper() + delta));
    }

    public CurrencyDisplayValues toDisplayValues(int totalCooper) {
        int normalized = Math.max(0, totalCooper);
        return new CurrencyDisplayValues(
                normalized / 10,
                normalized / 5,
                normalized
        );
    }
}

