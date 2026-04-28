package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.skills.model.SkillEffect;

import java.util.Locale;

public class SkillEffectBadgeFactory {

    public SkillEffectBadge create(SkillEffect effect) {
        return new SkillEffectBadge(
                "%s %s".formatted(effect.getDisplayName().toUpperCase(Locale.ROOT), effect.getValue()),
                colorByEffect(effect.getType())
        );
    }

    private String colorByEffect(String type) {
        if (type == null) {
            return "#55ccff";
        }
        return switch (type) {
            case "DAMAGE" -> "#ff5555";
            case "HEAL" -> "#55ff55";
            default -> "#55ccff";
        };
    }
}













