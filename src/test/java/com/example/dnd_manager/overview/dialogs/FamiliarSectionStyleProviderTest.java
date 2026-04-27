package com.example.dnd_manager.overview.dialogs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FamiliarSectionStyleProviderTest {

    private final FamiliarSectionStyleProvider provider = new FamiliarSectionStyleProvider();

    @Test
    void styleTokens_areProvidedForSections() {
        assertTrue(provider.resourcesContainerStyle().contains("-fx-background-color"));
        assertTrue(provider.iconHeaderStyle().contains("-fx-font-size"));
        assertTrue(provider.statBlockStyle().contains("-fx-padding"));
    }
}

