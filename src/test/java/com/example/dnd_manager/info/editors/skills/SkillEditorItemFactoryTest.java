package com.example.dnd_manager.info.editors.skills;

import com.example.dnd_manager.info.skills.model.Skill;
import com.example.dnd_manager.info.skills.model.SkillEffect;
import com.example.dnd_manager.info.skills.model.TypeEffects;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class SkillEditorItemFactoryTest {

    private final SkillEditorItemFactory factory = new SkillEditorItemFactory();

    @Test
    void create_copiesEffectsAndTrimsName() {
        ArrayList<SkillEffect> effects = new ArrayList<>();
        effects.add(SkillEffect.of(TypeEffects.DAMAGE.getName(), null, "1d6"));

        Skill skill = factory.create(" Fireball ", "desc", effects, "ACTION", "fire.png");

        assertEquals("Fireball", skill.name());
        assertEquals("desc", skill.description());
        assertEquals("ACTION", skill.activationType());
        assertEquals("fire.png", skill.iconPath());
        assertEquals(1, skill.effects().size());
        assertNotSame(effects, skill.effects());
    }
}












