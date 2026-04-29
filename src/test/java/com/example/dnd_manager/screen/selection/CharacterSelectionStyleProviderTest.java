package com.example.dnd_manager.screen.selection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterSelectionStyleProviderTest {

    private final CharacterSelectionStyleProvider provider = new CharacterSelectionStyleProvider();

    @Test
    void stylesUseAstralPaletteAndLayoutContracts() {
        assertTrue(provider.rootStyle().contains("#070b14"));
        assertTrue(provider.headerStyle().contains("-fx-background-radius: 18"));
        assertTrue(provider.contentPanelStyle().contains("rgba(75, 93, 127"));
        assertTrue(provider.cardStyle(false).contains("-fx-cursor: hand"));
        assertTrue(provider.cardStyle(true).contains("rgba(175, 196, 216"));
        assertTrue(provider.portraitFrameStyle().contains("-fx-border-radius: 16"));
        assertTrue(provider.openHintStyle().contains("#8fa4bd"));
        assertFalse(provider.rootStyle().contains("#c89b3c"));
    }

    @Test
    void buttonStylesKeepReadableActionContracts() {
        assertTrue(provider.actionButtonStyle(false).contains("-fx-background-radius: 8"));
        assertTrue(provider.actionButtonStyle(false).contains("#0c1018"));
        assertTrue(provider.secondaryButtonStyle(false).contains("#e9edf3"));
        assertTrue(provider.dangerButtonStyle(false).contains("#f1e7ee"));
    }
}
