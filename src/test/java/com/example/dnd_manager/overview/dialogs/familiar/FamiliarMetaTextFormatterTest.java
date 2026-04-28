package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FamiliarMetaTextFormatterTest {

    private final FamiliarMetaTextFormatter formatter = new FamiliarMetaTextFormatter();

    @Test
    void format_containsRaceClassAndLevel() {
        Character familiar = new Character();
        familiar.setRace("Elf");
        familiar.setCharacterClass("Mage");
        familiar.setLevel(7);

        String text = formatter.format(familiar);

        assertTrue(text.contains("Elf"));
        assertTrue(text.contains("Mage"));
        assertTrue(text.contains("7"));
    }
}













