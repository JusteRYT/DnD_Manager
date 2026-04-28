package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.skills.model.Skill;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillDescriptionTextFactoryTest {

    private final SkillDescriptionTextFactory factory = new SkillDescriptionTextFactory();

    @Test
    void briefText_containsActivationAndDescription() {
        Skill skill = new Skill("Fire", "Burns target", List.of(), "Action", "icon.png");

        String text = factory.briefText(skill);

        assertTrue(text.contains("Action"));
        assertTrue(text.contains("Burns target"));
    }

    @Test
    void needsScroll_usesLongDescriptionThreshold() {
        assertFalse(factory.needsScroll("short"));
        assertTrue(factory.needsScroll("x".repeat(400)));
    }
}













