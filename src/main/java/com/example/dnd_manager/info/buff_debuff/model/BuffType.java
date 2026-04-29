package com.example.dnd_manager.info.buff_debuff.model;

import com.example.dnd_manager.lang.I18n;

/**
 * Type of character effect.
 */
public enum BuffType {
    BUFF("buffType.buffName", "BUFF", "БАФ"),
    DEBUFF("buffType.debuffName", "DEBUFF", "ДЕБАФ");

    private final String key;
    private final String[] legacyValues;

    BuffType(String key, String... legacyValues) {
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
        for (BuffType type : values()) {
            if (type.matches(value)) {
                return type.name();
            }
        }
        return value;
    }

    public static String displayName(String value) {
        for (BuffType type : values()) {
            if (type.matches(value)) {
                return type.getName();
            }
        }
        return value == null ? "" : value;
    }
}












