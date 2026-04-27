package com.example.dnd_manager.overview.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ActiveEffectsStyleProviderTest {

    private final ActiveEffectsStyleProvider provider = new ActiveEffectsStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.titleStyle().contains("-fx-font-style"));
        assertTrue(provider.effectLabelStyle().contains("-fx-background-color"));
    }
}

