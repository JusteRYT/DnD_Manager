package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;

import java.util.ArrayList;
import java.util.List;

public class ActiveEffectsService {

    public List<ActiveEffectBadge> collect(Character character) {
        List<ActiveEffectBadge> badges = new ArrayList<>();

        List<InventoryItem> equippedItems = character.getInventory().stream()
                .filter(InventoryItem::isEquipped)
                .toList();

        for (InventoryItem item : equippedItems) {
            if (item.getCustomEffectName() != null && !item.getCustomEffectName().isBlank()) {
                badges.add(new ActiveEffectBadge(item.getCustomEffectName(), null));
                continue;
            }
            for (Buff buff : item.getAttachedBuffs()) {
                badges.add(new ActiveEffectBadge(formatBuffText(buff), buff.iconPath()));
            }
        }

        return badges;
    }

    private String formatBuffText(Buff buff) {
        return (buff.type() != null && !buff.type().isBlank())
                ? String.format("%s (%s)", buff.name(), buff.type())
                : buff.name();
    }
}

