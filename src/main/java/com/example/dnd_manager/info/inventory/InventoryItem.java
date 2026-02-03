package com.example.dnd_manager.info.inventory;

import lombok.Getter;

/**
 * Represents an item in character inventory.
 */
@Getter
public class InventoryItem {

    private  String name;
    private  String description;
    private  String iconPath;

    public InventoryItem() {

    }

    public InventoryItem(String name, String description, String iconPath) {
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
    }

}
