package com.example.dnd_manager.overview.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TopBarInfoStyleProviderTest {

    private final TopBarInfoStyleProvider provider = new TopBarInfoStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.nameStyle().contains("-fx-font-size"));
        assertTrue(provider.levelBoxStyle().contains("-fx-border-width"));
        assertTrue(provider.leftBoxStyle().contains("-fx-background-color"));
    }
}

