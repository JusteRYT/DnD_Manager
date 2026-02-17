package com.example.dnd_manager.info.inventory;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an item in character inventory.
 */
@Getter
@Setter
public class InventoryItem {

    private String name, description, iconPath;
    private int count;


    public InventoryItem() {

    }

    public InventoryItem(String name, String description, String iconPath) {
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        this.count = 1;
    }

    public InventoryItem(String name, String description, String iconPath, int count) {
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        this.count = count;
    }
}
