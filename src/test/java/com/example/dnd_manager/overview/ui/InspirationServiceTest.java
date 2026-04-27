package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InspirationServiceTest {

    private final InspirationService service = new InspirationService();

    @Test
    void adjust_incrementsValue() {
        Character character = new Character();
        character.setInspiration(2);

        int value = service.adjust(character, 1);

        assertEquals(3, value);
        assertEquals(3, character.getInspiration());
    }

    @Test
    void adjust_clampsToZero() {
        Character character = new Character();
        character.setInspiration(0);

        int value = service.adjust(character, -1);

        assertEquals(0, value);
        assertEquals(0, character.getInspiration());
    }
}

