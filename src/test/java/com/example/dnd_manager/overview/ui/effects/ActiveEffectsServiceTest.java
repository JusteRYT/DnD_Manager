package com.example.dnd_manager.overview.ui.effects;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ActiveEffectsServiceTest {

    private final ActiveEffectsService service = new ActiveEffectsService();

    @Test
    void collect_usesCustomEffectNameWhenPresent() {
        Character character = new Character();
        InventoryItem item = new InventoryItem("Ring", "Desc", "icon.png");
        item.setEquipped(true);
        item.setCustomEffectName("Aura");
        item.getAttachedBuffs().add(new Buff("Haste", "desc", "Buff", "b.png"));
        character.getInventory().add(item);

        List<ActiveEffectBadge> badges = service.collect(character);

        assertEquals(1, badges.size());
        assertEquals("Aura", badges.getFirst().text());
        assertNull(badges.getFirst().iconPath());
    }

    @Test
    void collect_usesBuffsWhenNoCustomEffect() {
        Character character = new Character();
        InventoryItem item = new InventoryItem("Boots", "Desc", "icon.png");
        item.setEquipped(true);
        item.setCustomEffectName("");
        item.getAttachedBuffs().add(new Buff("Speed", "desc", "Buff", "speed.png"));
        character.getInventory().add(item);

        List<ActiveEffectBadge> badges = service.collect(character);

        assertEquals(1, badges.size());
        assertEquals("Speed (Buff)", badges.getFirst().text());
        assertEquals("speed.png", badges.getFirst().iconPath());
    }

    @Test
    void collect_ignoresUnequippedItems() {
        Character character = new Character();
        InventoryItem item = new InventoryItem("Cloak", "Desc", "icon.png");
        item.setEquipped(false);
        item.setCustomEffectName("Stealth");
        character.getInventory().add(item);

        List<ActiveEffectBadge> badges = service.collect(character);

        assertEquals(0, badges.size());
    }
}












