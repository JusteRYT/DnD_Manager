package com.example.dnd_manager.info.inventory;

/**
 * Represents an item in character inventory.
 */
public class InventoryItem {

    private final String name;
    private final String description;
    private final String iconPath;

    public InventoryItem(String name, String description, String iconPath) {
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconPath() {
        return iconPath;
    }
}
