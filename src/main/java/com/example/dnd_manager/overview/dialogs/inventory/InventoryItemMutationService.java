package com.example.dnd_manager.overview.dialogs.inventory;

import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.info.skills.model.Skill;

import java.util.ArrayList;
import java.util.List;

public class InventoryItemMutationService {

    public InventoryItem createNew(
            String name,
            String description,
            int count,
            String iconPath,
            String defaultIconPath,
            boolean equipped,
            String customEffectName,
            List<Buff> attachedBuffs,
            List<Skill> attachedSkills
    ) {
        InventoryItem item = new InventoryItem(
                name,
                description,
                normalizeIconPath(iconPath, defaultIconPath)
        );
        item.setCount(count);
        item.setEquipped(equipped);
        item.setCustomEffectName(customEffectName);
        item.setAttachedBuffs(new ArrayList<>(attachedBuffs));
        item.setAttachedSkills(new ArrayList<>(attachedSkills));
        return item;
    }

    public void applyToExisting(
            InventoryItem target,
            String name,
            String description,
            int count,
            String iconPath,
            String defaultIconPath,
            boolean equipped,
            String customEffectName,
            List<Buff> attachedBuffs,
            List<Skill> attachedSkills
    ) {
        target.setName(name);
        target.setDescription(description);
        target.setCount(count);
        target.setIconPath(normalizeIconPath(iconPath, defaultIconPath));
        target.setEquipped(equipped);
        target.setCustomEffectName(customEffectName);
        target.setAttachedBuffs(new ArrayList<>(attachedBuffs));
        target.setAttachedSkills(new ArrayList<>(attachedSkills));
    }

    private String normalizeIconPath(String iconPath, String defaultIconPath) {
        if (iconPath == null || iconPath.isBlank()) {
            return defaultIconPath;
        }
        return iconPath;
    }
}












