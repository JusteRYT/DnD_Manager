package com.example.dnd_manager.info.skills.model;

import com.example.dnd_manager.lang.I18n;

public enum TypeEffects {
    DAMAGE("skill.effectType.damage", "DAMAGE", "УРОН"),
    HEAL("skill.effectType.heal", "HEAL", "ЛЕЧЕНИЕ"),
    DICE_INCREASE("skill.effectType.diceIncrease", "DICE INCREASE", "УВЕЛИЧЕНИЕ К БРОСКУ"),
    DICE_DECREASE("skill.effectType.diceDecrease", "DICE DECREASE", "УМЕНЬШЕНИЕ К БРОСКУ"),
    INCREASE_ARMOR("skill.effectType.armorIncrease", "INCREASE ARMOR", "УВЕЛИЧЕНИЕ БРОНИ"),
    DECREASE_ARMOR("skill.effectType.armorDecrease", "DECREASE ARMOR", "УМЕНЬШЕНИЕ БРОНИ"),
    CUSTOM("skill.effectType.custom", "CUSTOM", "ПОЛЬЗОВАТЕЛЬСКИЙ");

    private final String key;
    private final String[] legacyValues;

    TypeEffects(String key, String... legacyValues) {
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
        for (TypeEffects type : values()) {
            if (type.matches(value)) {
                return type.name();
            }
        }
        return value;
    }

    public static String displayName(String value) {
        for (TypeEffects type : values()) {
            if (type.matches(value)) {
                return type.getName();
            }
        }
        return value == null ? "" : value;
    }
}












