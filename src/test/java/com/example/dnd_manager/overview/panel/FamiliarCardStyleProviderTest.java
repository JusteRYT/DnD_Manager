package com.example.dnd_manager.overview.panel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FamiliarCardStyleProviderTest {

    private final FamiliarCardStyleProvider provider = new FamiliarCardStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.nameLabelStyle().contains("-fx-font-size"));
        assertTrue(provider.cardIdleStyle().contains("-fx-border-color"));
        assertTrue(provider.cardHoverStyle().contains("-fx-effect"));
    }
}













