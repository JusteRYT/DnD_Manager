package com.example.dnd_manager.info.skills.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillCardStyleProviderTest {

    private final SkillCardStyleProvider provider = new SkillCardStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.cardIdleStyle().contains("-fx-border-color"));
        assertTrue(provider.cardHoverStyle().contains("-fx-effect"));
        assertTrue(provider.headerBandStyle().contains("-fx-background-radius: 12"));
        assertTrue(provider.effectBadgeStyle("#ff5555").contains("#ff5555"));
        assertTrue(provider.activationBadgeStyle().contains("-fx-background-radius: 999"));
        assertTrue(provider.descriptionPanelStyle().contains("-fx-background-radius: 10"));
        assertTrue(provider.effectsPanelStyle().contains("-fx-background-radius: 10"));
        assertTrue(provider.sectionCaptionStyle().contains("-fx-font-weight"));
        assertTrue(provider.sourceInfoStyle().contains("#b7c9dd"));
    }
}













