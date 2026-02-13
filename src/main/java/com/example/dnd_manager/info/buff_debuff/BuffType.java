package com.example.dnd_manager.info.buff_debuff;

import com.example.dnd_manager.lang.I18n;
import lombok.Getter;

/**
 * Type of character effect.
 */
public enum BuffType {
    BUFF(I18n.t("buffType.buffName")),
    DEBUFF(I18n.t("buffType.debuffName")),;



    @Getter
    private final String name;

    BuffType(String name) {
        this.name = name;
    }
}
