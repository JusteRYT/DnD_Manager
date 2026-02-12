package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.lang.I18n;
import lombok.Getter;

/**
 * Defines how a skill is activated.
 */
public enum ActivationType {
    PASSIVE(I18n.t("skill.activationType.passive")),
    ACTION(I18n.t("skill.activationType.action")),
    REACTION(I18n.t("skill.activationType.reaction")),;

    @Getter
    private final String name;

    ActivationType(String name) {
        this.name = name;
    }
}
