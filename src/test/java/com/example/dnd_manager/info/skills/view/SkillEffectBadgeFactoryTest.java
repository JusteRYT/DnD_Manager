package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.skills.model.SkillEffect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillEffectBadgeFactoryTest {

    private final SkillEffectBadgeFactory factory = new SkillEffectBadgeFactory();

    @Test
    void create_formatsDamageBadge() {
        SkillEffectBadge badge = factory.create(new SkillEffect("DAMAGE", null, "2d6"));

        assertEquals("DAMAGE 2d6", badge.text());
        assertEquals("#c56f82", badge.color());
    }

    @Test
    void create_usesDefaultColorForUnknownType() {
        SkillEffectBadge badge = factory.create(new SkillEffect("RANGE", null, "30"));

        assertEquals("RANGE 30", badge.text());
        assertEquals("#9fb2c8", badge.color());
    }
}













