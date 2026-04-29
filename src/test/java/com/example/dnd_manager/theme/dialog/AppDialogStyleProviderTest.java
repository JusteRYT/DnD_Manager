package com.example.dnd_manager.theme.dialog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppDialogStyleProviderTest {

    private final AppDialogStyleProvider provider = new AppDialogStyleProvider();

    @Test
    void rootAndPanelsUseApplicationPalette() {
        assertTrue(provider.rootStyle().contains("#070b14"));
        assertTrue(provider.rootStyle().contains("rgba(127, 185, 212"));
        assertTrue(provider.contentAreaStyle().contains("rgba(10, 16, 31"));
        assertTrue(provider.panelStyle().contains("rgba(17, 23, 41"));
        assertFalse(provider.rootStyle().contains("#1e1e1e"));
        assertFalse(provider.panelStyle().contains("#2b2b2b"));
    }

    @Test
    void controlsAvoidLegacyGoldAndGrayPalette() {
        assertTrue(provider.primaryButtonStyle(false).contains("#dfe6ec"));
        assertTrue(provider.secondaryButtonStyle(false).contains("#e9edf3"));
        assertTrue(provider.dangerButtonStyle(false).contains("#f1e7ee"));
        assertTrue(provider.progressBarStyle().contains("#b7c9dd"));
        assertFalse(provider.primaryButtonStyle(false).contains("#FFC107"));
        assertFalse(provider.progressBarStyle().contains("#ffaa00"));
    }

    @Test
    void textInputHasReadableFocusAndBaseStates() {
        assertTrue(provider.textInputStyle(false).contains("rgba(75, 93, 127"));
        assertTrue(provider.textInputStyle(true).contains("rgba(175, 196, 216"));
        assertTrue(provider.textInputStyle(false).contains("#10172a"));
    }
}
