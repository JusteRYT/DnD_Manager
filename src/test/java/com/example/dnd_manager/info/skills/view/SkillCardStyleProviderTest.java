package com.example.dnd_manager.info.skills.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillCardStyleProviderTest {

    private final SkillCardStyleProvider provider = new SkillCardStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.cardIdleStyle().contains("-fx-border-color"));
        assertTrue(provider.cardHoverStyle().contains("-fx-effect"));
        assertTrue(provider.effectBadgeStyle("#ff5555").contains("#ff5555"));
    }
}













