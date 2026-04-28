package com.example.dnd_manager.screen.form;

import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterCoreFormDataApplierTest {

    private final CharacterCoreFormDataApplier applier = new CharacterCoreFormDataApplier();

    @Test
    void apply_mapsCoreFormDataToCharacter() {
        Character character = new Character();
        CharacterCoreFormData data = new CharacterCoreFormData(
                "Hero",
                "Elf",
                "Mage",
                18,
                14,
                9,
                3,
                "avatar.png",
                "Description",
                "Personality",
                "Backstory"
        );

        applier.apply(character, data);

        assertEquals("Hero", character.getName());
        assertEquals("Elf", character.getRace());
        assertEquals("Mage", character.getCharacterClass());
        assertEquals(18, character.getCurrentHp());
        assertEquals(18, character.getMaxHp());
        assertEquals(14, character.getArmor());
        assertEquals(9, character.getCurrentMana());
        assertEquals(9, character.getMaxMana());
        assertEquals(3, character.getLevel());
        assertEquals("avatar.png", character.getAvatarImage());
        assertEquals("Description", character.getDescription());
        assertEquals("Personality", character.getPersonality());
        assertEquals("Backstory", character.getBackstory());
    }
}













