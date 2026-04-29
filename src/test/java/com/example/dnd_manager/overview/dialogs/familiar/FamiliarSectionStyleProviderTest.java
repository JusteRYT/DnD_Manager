package com.example.dnd_manager.overview.dialogs.familiar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FamiliarSectionStyleProviderTest {

    private final FamiliarSectionStyleProvider provider = new FamiliarSectionStyleProvider();

    @Test
    void styleTokens_areProvidedForSections() {
        assertTrue(provider.resourcesContainerStyle().contains("-fx-background-color"));
        assertTrue(provider.iconHeaderStyle().contains("-fx-font-size"));
        assertTrue(provider.statBlockStyle().contains("-fx-padding"));
        assertFalse(provider.iconHeaderStyle().contains("#9c27b0"));
        assertFalse(provider.statBlockStyle().contains("#2b2b2b"));
    }
}













