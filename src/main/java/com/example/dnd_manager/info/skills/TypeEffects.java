package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.lang.I18n;
import lombok.Getter;

public enum TypeEffects {
    DAMAGE(I18n.t("skill.effectType.damage")),
    HEAL(I18n.t("skill.effectType.heal")),
    DICE_INCREASE(I18n.t("skill.effectType.diceIncrease")),
    DICE_DECREASE(I18n.t("skill.effectType.diceDecrease")),
    INCREASE_ARMOR(I18n.t("skill.effectType.armorIncrease")),
    DECREASE_ARMOR(I18n.t("skill.effectType.armorDecrease")),
    CUSTOM(I18n.t("skill.effectType.custom")),;

    @Getter
    private final String name;

    TypeEffects(String name) {
        this.name = name;
    }
}
