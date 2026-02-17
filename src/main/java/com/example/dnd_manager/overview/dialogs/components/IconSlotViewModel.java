package com.example.dnd_manager.overview.dialogs.components;

import lombok.Getter;

import java.util.Map;

/**
 * View model used by IconSlot UI component.
 * Contains only display data and is not serialized to JSON.
 */

@Getter
public class IconSlotViewModel {

    private final String name;
    private final String iconPath;
    private final String accentColor;
    private final String badgeText;
    private final String typeName;
    private final Map<String, String> attributes;
    private final String description;

    /**
     * Creates view model.
     */
    public IconSlotViewModel(
            String name,
            String iconPath,
            String accentColor,
            String badgeText,
            String typeName,
            Map<String, String> attributes,
            String description) {

        this.name = name;
        this.iconPath = iconPath;
        this.accentColor = accentColor;
        this.badgeText = badgeText;
        this.typeName = typeName;
        this.attributes = attributes;
        this.description = description;
    }

}
