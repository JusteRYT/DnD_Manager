package com.example.dnd_manager.overview.ui.topbar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TopBarActionsStyleProviderTest {

    private final TopBarActionsStyleProvider provider = new TopBarActionsStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.actionsRowStyle().contains("-fx-border-color"));
        assertTrue(provider.actionsRowStyle().contains("-fx-effect"));
    }
}













