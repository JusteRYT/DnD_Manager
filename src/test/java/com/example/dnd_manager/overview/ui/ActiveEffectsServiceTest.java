package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals("Aura", badges.get(0).text());
        assertEquals(null, badges.get(0).iconPath());
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
        assertEquals("Speed (Buff)", badges.get(0).text());
        assertEquals("speed.png", badges.get(0).iconPath());
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
