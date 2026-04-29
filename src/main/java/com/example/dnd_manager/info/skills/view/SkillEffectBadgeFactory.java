package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.skills.model.SkillEffect;
import com.example.dnd_manager.info.skills.model.TypeEffects;

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
            return "#9fb2c8";
        }
        if (TypeEffects.DAMAGE.matches(type)) return "#c56f82";
        if (TypeEffects.HEAL.matches(type)) return "#7ebd9b";
        if (TypeEffects.INCREASE_ARMOR.matches(type) || TypeEffects.DECREASE_ARMOR.matches(type)) return "#8fb3d8";
        if (TypeEffects.DICE_INCREASE.matches(type) || TypeEffects.DICE_DECREASE.matches(type)) return "#b5a1d8";
        return "#9fb2c8";
    }
}













