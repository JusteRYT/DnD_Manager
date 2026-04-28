package com.example.dnd_manager.info.editors.familiar;

import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FamiliarCoreDataApplierTest {

    private final FamiliarCoreDataApplier applier = new FamiliarCoreDataApplier();

    @Test
    void apply_mapsCoreDataToFamiliar() {
        Character familiar = new Character();

        applier.apply(familiar, new FamiliarCoreData(
                "Wolf",
                "Beast",
                "Companion",
                "12 hp",
                "14",
                "6 mana",
                "wolf.png"
        ));

        assertEquals("Wolf", familiar.getName());
        assertEquals("Beast", familiar.getRace());
        assertEquals("Companion", familiar.getCharacterClass());
        assertEquals(12, familiar.getMaxHp());
        assertEquals(12, familiar.getCurrentHp());
        assertEquals(14, familiar.getArmor());
        assertEquals(6, familiar.getMaxMana());
        assertEquals(6, familiar.getCurrentMana());
        assertEquals("wolf.png", familiar.getAvatarImage());
    }

    @Test
    void parseSafe_returnsZeroForBlankOrNonNumeric() {
        assertEquals(0, applier.parseSafe(""));
        assertEquals(0, applier.parseSafe("abc"));
    }
}













