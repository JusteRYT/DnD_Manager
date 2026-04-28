package com.example.dnd_manager.overview.ui.inspiration;

import com.example.dnd_manager.domain.Character;

public class InspirationService {

    public int adjust(Character character, int delta) {
        int next = Math.max(0, character.getInspiration() + delta);
        character.setInspiration(next);
        return next;
    }
}













