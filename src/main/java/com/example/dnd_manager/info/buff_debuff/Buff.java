package com.example.dnd_manager.info.buff_debuff;

import com.example.dnd_manager.info.familiar.Displayable;

/**
 * Represents a buff or debuff applied to a character.
 */
public record Buff(String name, String description, String type, String iconPath)
        implements Displayable {

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getIconPath() {
        return iconPath;
    }

    @Override
    public String getTypeName() {
        return type.toUpperCase();
    }

    @Override
    public String getAccentColor() {
        return "BUFF".equalsIgnoreCase(type) ? "#69db7c" : "#ff6b6b";
    }
}
