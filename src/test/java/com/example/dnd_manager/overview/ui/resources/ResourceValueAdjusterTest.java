package com.example.dnd_manager.overview.ui.resources;

import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceValueAdjusterTest {

    private final ResourceValueAdjuster adjuster = new ResourceValueAdjuster();

    @Test
    void change_clampsToMax() {
        Character character = new Character();
        character.setMaxHp(10);
        character.setCurrentHp(9);

        int result = adjuster.change(character, 5, new CharacterHpResourceMetric());

        assertEquals(10, result);
        assertEquals(10, character.getCurrentHp());
    }

    @Test
    void change_clampsToZero() {
        Character character = new Character();
        character.setMaxMana(6);
        character.setCurrentMana(1);

        int result = adjuster.change(character, -5, new CharacterManaResourceMetric());

        assertEquals(0, result);
        assertEquals(0, character.getCurrentMana());
    }

    @Test
    void change_handlesNegativeMaxAsZero() {
        Character character = new Character();
        character.setMaxMana(-4);
        character.setCurrentMana(3);

        int result = adjuster.change(character, 1, new CharacterManaResourceMetric());

        assertEquals(0, result);
        assertEquals(0, character.getCurrentMana());
    }
}













