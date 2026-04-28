package com.example.dnd_manager.overview.panel;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourcePanelStyleProviderTest {

    @Test
    void containerStyle_containsCoreVisualTokens() {
        String style = new ResourcePanelStyleProvider().containerStyle();

        assertTrue(style.contains("-fx-background-color"));
        assertTrue(style.contains("-fx-border-color"));
        assertTrue(style.contains("-fx-effect"));
    }
}













