package com.example.dnd_manager.info.inventory;

import com.example.dnd_manager.info.familiar.Displayable;
import com.example.dnd_manager.lang.I18n;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents an item in character inventory.
 */
@Getter
@Setter
public class InventoryItem implements Displayable {

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

    @Override
    public String getTypeName() {
        return "ITEM";
    }

    @Override
    public String getAccentColor() {
        return "#ffd43b";
    }

    @Override
    public String getBadgeText() {
        return count > 1 ? "x" + count : null;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = new LinkedHashMap<>();
        attr.put(I18n.t("textField.inventoryCountPrompt"), String.valueOf(count));
        return attr;
    }

}
