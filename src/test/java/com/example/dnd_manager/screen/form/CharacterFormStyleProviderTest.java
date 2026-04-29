package com.example.dnd_manager.screen.form;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterFormStyleProviderTest {

    private final CharacterFormStyleProvider provider = new CharacterFormStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.formStyle().contains("#070b14"));
        assertTrue(provider.formStyle().contains("#11172a"));
        assertTrue(provider.titlePanelStyle().contains("-fx-border-width"));
        assertTrue(provider.screenSubtitleStyle().contains("#9fb2c8"));
        assertTrue(provider.heroCardStyle().contains("-fx-background-radius: 20"));
        assertTrue(provider.heroColumnStyle().contains("-fx-border-color: transparent"));
        assertTrue(provider.statsSectionStyle().contains("-fx-border-color"));
        assertTrue(provider.statsTitleStyle().contains("#f0f2f7"));
        assertTrue(provider.magicalBorderStyle().contains("rgba(17, 23, 41"));
        assertTrue(provider.screenTitleStyle().contains("dropshadow"));
    }

    @Test
    void stylesStayAlignedWithStartScreenAstralPalette() {
        String combined = provider.formStyle()
                + provider.heroCardStyle()
                + provider.statsSectionStyle()
                + provider.magicalBorderStyle()
                + provider.actionBarStyle()
                + provider.sectionSwitchStyle();

        assertTrue(combined.contains("rgba(75, 93, 127"));
        assertTrue(combined.contains("rgba(196, 189, 214"));
        assertFalse(combined.contains("#17253a"));
        assertFalse(combined.contains("rgba(36, 59, 90"));
    }
}












