package com.example.dnd_manager.overview.dialogs.inventory;

import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.info.skills.model.Skill;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class InventoryItemFormState {

    private final InventoryItem existingItem;
    @Setter
    private String iconPath;
    private final List<Buff> attachedBuffs;
    private final List<Skill> attachedSkills;

    public InventoryItemFormState(InventoryItem existingItem) {
        this.existingItem = existingItem;
        this.iconPath = existingItem != null ? existingItem.getIconPath() : null;
        this.attachedBuffs = existingItem != null
                ? new ArrayList<>(existingItem.getAttachedBuffs())
                : new ArrayList<>();
        this.attachedSkills = existingItem != null
                ? new ArrayList<>(existingItem.getAttachedSkills())
                : new ArrayList<>();
    }

    public InventoryItem existingItem() {
        return existingItem;
    }

    public String iconPath() {
        return iconPath;
    }

    public List<Buff> attachedBuffs() {
        return attachedBuffs;
    }

    public List<Skill> attachedSkills() {
        return attachedSkills;
    }
}













