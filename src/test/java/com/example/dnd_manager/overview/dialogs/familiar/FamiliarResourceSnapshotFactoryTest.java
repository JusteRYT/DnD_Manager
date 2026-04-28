package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FamiliarResourceSnapshotFactoryTest {

    private final FamiliarResourceSnapshotFactory factory = new FamiliarResourceSnapshotFactory();

    @Test
    void from_mapsCharacterValuesToDisplayStrings() {
        Character familiar = new Character();
        familiar.setCurrentHp(7);
        familiar.setMaxHp(12);
        familiar.setCurrentMana(2);
        familiar.setMaxMana(8);
        familiar.setArmor(15);
        familiar.setLevel(4);

        FamiliarResourceSnapshot snapshot = factory.from(familiar);

        assertEquals("7/12", snapshot.hpText());
        assertEquals("2/8", snapshot.mpText());
        assertEquals("15", snapshot.acText());
        assertEquals("4", snapshot.levelText());
    }
}













