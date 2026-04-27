package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.info.inventory.InventoryItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class InventoryItemFormStateTest {

    @Test
    void constructor_copiesExistingItemData() {
        InventoryItem existing = new InventoryItem("Sword", "desc", "icon.png");
        existing.getAttachedBuffs().add(null);
        existing.getAttachedSkills().add(null);

        InventoryItemFormState state = new InventoryItemFormState(existing);

        assertEquals("icon.png", state.iconPath());
        assertEquals(1, state.attachedBuffs().size());
        assertEquals(1, state.attachedSkills().size());
        assertNotSame(existing.getAttachedBuffs(), state.attachedBuffs());
        assertNotSame(existing.getAttachedSkills(), state.attachedSkills());
    }

    @Test
    void setIconPath_updatesValue() {
        InventoryItemFormState state = new InventoryItemFormState(null);

        state.setIconPath("new-icon.png");

        assertEquals("new-icon.png", state.iconPath());
    }
}

