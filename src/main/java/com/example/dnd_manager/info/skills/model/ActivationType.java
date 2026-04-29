package com.example.dnd_manager.info.skills.model;

import com.example.dnd_manager.lang.I18n;

/**
 * Defines how a skill is activated.
 */
public enum ActivationType {
    ACTION("skill.activationType.action", "ACTION", "АКТИВНЫЙ"),
    PASSIVE("skill.activationType.passive", "PASSIVE", "ПАССИВНЫЙ"),
    REACTION("skill.activationType.reaction", "REACTION", "РЕАКЦИЯ");

    private final String key;
    private final String[] legacyValues;

    ActivationType(String key, String... legacyValues) {
        this.key = key;
        this.legacyValues = legacyValues;
    }

    public String getName() {
        return I18n.t(key);
    }

    public boolean matches(String value) {
        if (value == null) {
            return false;
        }
        if (name().equalsIgnoreCase(value) || getName().equalsIgnoreCase(value)) {
            return true;
        }
        for (String legacyValue : legacyValues) {
            if (legacyValue.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    public static String canonical(String value) {
        for (ActivationType type : values()) {
            if (type.matches(value)) {
                return type.name();
            }
        }
        return value;
    }

    public static String displayName(String value) {
        for (ActivationType type : values()) {
            if (type.matches(value)) {
                return type.getName();
            }
        }
        return value == null ? "" : value;
    }
}












