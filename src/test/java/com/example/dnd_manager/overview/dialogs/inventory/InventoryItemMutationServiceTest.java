package com.example.dnd_manager.overview.dialogs.inventory;

import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.info.skills.model.Skill;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryItemMutationServiceTest {

    private static final String DEFAULT_ICON = "icon/no_image.png";

    private final InventoryItemMutationService service = new InventoryItemMutationService();

    @Test
    void createNew_usesDefaultIconWhenInputPathBlank() {
        List<Buff> buffs = new ArrayList<>();
        buffs.add(new Buff("Buff", "desc", "BUFF", "icon/a.png"));
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill("Skill", "desc", List.of(), "ACTION", "icon/s.png"));

        InventoryItem item = service.createNew(
                "Sword",
                "desc",
                2,
                "",
                DEFAULT_ICON,
                true,
                "+1 AC",
                buffs,
                skills
        );

        assertEquals("Sword", item.getName());
        assertEquals(DEFAULT_ICON, item.getIconPath());
        assertEquals(2, item.getCount());
        assertTrue(item.isEquipped());
        assertEquals("+1 AC", item.getCustomEffectName());
        assertEquals(1, item.getAttachedBuffs().size());
        assertEquals(1, item.getAttachedSkills().size());
        assertNotSame(buffs, item.getAttachedBuffs());
        assertNotSame(skills, item.getAttachedSkills());
    }

    @Test
    void applyToExisting_updatesFieldsAndCopiesCollections() {
        InventoryItem target = new InventoryItem("Old", "old", "icon/old.png");
        List<Buff> buffs = new ArrayList<>();
        buffs.add(new Buff("Buff", "desc", "BUFF", "icon/a.png"));
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill("Skill", "desc", List.of(), "ACTION", "icon/s.png"));

        service.applyToExisting(
                target,
                "New",
                "new desc",
                5,
                null,
                DEFAULT_ICON,
                false,
                "Custom",
                buffs,
                skills
        );

        assertEquals("New", target.getName());
        assertEquals("new desc", target.getDescription());
        assertEquals(5, target.getCount());
        assertEquals(DEFAULT_ICON, target.getIconPath());
        assertFalse(target.isEquipped());
        assertEquals("Custom", target.getCustomEffectName());
        assertEquals(1, target.getAttachedBuffs().size());
        assertEquals(1, target.getAttachedSkills().size());
        assertNotSame(buffs, target.getAttachedBuffs());
        assertNotSame(skills, target.getAttachedSkills());
    }
}












