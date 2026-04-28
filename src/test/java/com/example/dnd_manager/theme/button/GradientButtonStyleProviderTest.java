package com.example.dnd_manager.theme.button;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GradientButtonStyleProviderTest {

    private final GradientButtonStyleProvider provider = new GradientButtonStyleProvider();

    @Test
    void valueAdjustStyles_includeColorsAndInteractionTokens() {
        assertTrue(provider.valueAdjustBaseStyle("#123456", "rgba(1, 2, 3, 0.4)").contains("#123456"));
        assertTrue(provider.valueAdjustHoverStyle("#abcdef", "rgba(1, 2, 3, 0.4)").contains("#abcdef"));
        assertTrue(provider.valueAdjustCommonStyle().contains("-fx-cursor: hand"));
    }

    @Test
    void actionStyles_includeExpectedVisualTokens() {
        assertTrue(provider.editIconBaseStyle().contains("#FFC107"));
        assertTrue(provider.editIconHoverStyle().contains("#ffd54f"));
        assertTrue(provider.deleteButtonStyle("#ff0000", true).contains("rgba(255, 0, 0, 0.6)"));
        assertTrue(provider.primaryGradientStyle(16, false).contains("-fx-font-size: 16px"));
        assertTrue(provider.primaryGradientStyle(16, true).contains("15, 0, 0, 0"));
    }
}












