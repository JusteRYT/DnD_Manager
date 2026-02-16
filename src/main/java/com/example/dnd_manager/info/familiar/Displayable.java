package com.example.dnd_manager.info.familiar;

import java.util.Map;
import java.util.LinkedHashMap;

public interface Displayable {
    String getName();
    String getDescription();
    String getIconPath();
    String getTypeName();    // "Skill", "Item", "Buff"
    String getAccentColor(); // Цвет рамки

    default Map<String, String> getAttributes() {
        return new LinkedHashMap<>();
    }

    default String getBadgeText() {
        return null;
    }
}
