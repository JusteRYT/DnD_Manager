package com.example.dnd_manager.overview.panel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FamiliarsPanelStyleProviderTest {

    private final FamiliarsPanelStyleProvider provider = new FamiliarsPanelStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.titleStyle().contains("-fx-text-fill"));
        assertTrue(provider.idleStyle().contains("-fx-background-color"));
        assertTrue(provider.hoverStyle().contains("-fx-effect"));
    }
}













