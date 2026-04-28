package com.example.dnd_manager.overview.dialogs.familiar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FamiliarHeaderStyleProviderTest {

    private final FamiliarHeaderStyleProvider provider = new FamiliarHeaderStyleProvider();

    @Test
    void styleTokens_areProvidedForHeader() {
        assertTrue(provider.nameStyle().contains("-fx-font-size"));
        assertTrue(provider.metaStyle().contains("-fx-text-fill"));
    }
}













