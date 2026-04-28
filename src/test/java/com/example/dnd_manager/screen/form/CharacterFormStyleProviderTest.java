package com.example.dnd_manager.screen.form;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterFormStyleProviderTest {

    private final CharacterFormStyleProvider provider = new CharacterFormStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.formStyle().contains("transparent"));
        assertTrue(provider.statsSectionStyle().contains("-fx-border-color"));
        assertTrue(provider.statsTitleStyle().contains("#FFC107"));
        assertTrue(provider.magicalBorderStyle().contains("linear-gradient"));
    }
}












