package com.example.dnd_manager.info.editors.inventory;

import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.info.skills.model.Skill;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryEditorItemFactoryTest {

    private final InventoryEditorItemFactory factory = new InventoryEditorItemFactory();

    @Test
    void create_copiesAttachedEffectsAndTrimsName() {
        List<Buff> buffs = new ArrayList<>(List.of(new Buff("Bless", "desc", "BUFF", "buff.png")));
        List<Skill> skills = new ArrayList<>(List.of(new Skill("Slash", "desc", List.of(), "ACTION", "slash.png")));

        InventoryItem item = factory.create(" Sword ", "desc", "sword.png", 2, buffs, skills);
        buffs.clear();
        skills.clear();

        assertEquals("Sword", item.getName());
        assertEquals("desc", item.getDescription());
        assertEquals("sword.png", item.getIconPath());
        assertEquals(2, item.getCount());
        assertEquals(1, item.getAttachedBuffs().size());
        assertEquals(1, item.getAttachedSkills().size());
    }
}












