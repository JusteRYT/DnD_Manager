package com.example.dnd_manager.info.inventory.model;

import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.skills.model.Skill;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item in character inventory.
 */
@Getter
@Setter
public class InventoryItem {

    private String name, description, iconPath;
    private int count;
    private boolean isEquipped;
    private String customEffectName;

    private List<Buff> attachedBuffs = new ArrayList<>();
    private List<Skill> attachedSkills = new ArrayList<>();


    public InventoryItem() {

    }

    public InventoryItem(String name, String description, String iconPath) {
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        this.count = 1;
        this.isEquipped = false;
    }

    public InventoryItem(String name, String description, String iconPath, int count) {
        this(name, description, iconPath);
        this.count = count;
    }
}












