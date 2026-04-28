package com.example.dnd_manager.overview.ui.resources;

import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterResourceMetricTest {

    @Test
    void hpMetric_readsAndWritesHpFields() {
        Character character = new Character();
        character.setCurrentHp(7);
        character.setMaxHp(14);
        CharacterResourceMetric metric = new CharacterHpResourceMetric();

        assertEquals(7, metric.getCurrent(character));
        assertEquals(14, metric.getMax(character));

        metric.setCurrent(character, 9);
        assertEquals(9, character.getCurrentHp());
    }

    @Test
    void manaMetric_readsAndWritesManaFields() {
        Character character = new Character();
        character.setCurrentMana(3);
        character.setMaxMana(8);
        CharacterResourceMetric metric = new CharacterManaResourceMetric();

        assertEquals(3, metric.getCurrent(character));
        assertEquals(8, metric.getMax(character));

        metric.setCurrent(character, 6);
        assertEquals(6, character.getCurrentMana());
    }
}













