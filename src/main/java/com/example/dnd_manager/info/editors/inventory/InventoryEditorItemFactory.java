package com.example.dnd_manager.info.editors.inventory;

import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.info.skills.model.Skill;

import java.util.ArrayList;
import java.util.List;

public class InventoryEditorItemFactory {

    public InventoryItem create(
            String name,
            String description,
            String iconPath,
            int count,
            List<Buff> attachedBuffs,
            List<Skill> attachedSkills
    ) {
        InventoryItem item = new InventoryItem(name.trim(), description, iconPath, count);
        item.getAttachedBuffs().addAll(new ArrayList<>(attachedBuffs));
        item.getAttachedSkills().addAll(new ArrayList<>(attachedSkills));
        return item;
    }
}












