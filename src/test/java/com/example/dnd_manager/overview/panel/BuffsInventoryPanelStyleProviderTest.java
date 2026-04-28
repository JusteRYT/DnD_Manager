package com.example.dnd_manager.overview.panel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BuffsInventoryPanelStyleProviderTest {

    private final BuffsInventoryPanelStyleProvider provider = new BuffsInventoryPanelStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.buffsWrapperIdleStyle().contains("-fx-background-color"));
        assertTrue(provider.buffsWrapperIdleStyle().contains("-fx-border-color"));
        assertTrue(provider.buffsWrapperHoverStyle().contains("-fx-effect"));
    }
}













