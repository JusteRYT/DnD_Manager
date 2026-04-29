package com.example.dnd_manager.info.editors.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityEditorStyleProviderTest {

    private final EntityEditorStyleProvider provider = new EntityEditorStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.titleStyle().contains("-fx-font-size"));
        assertTrue(provider.inputCardStyle().contains("-fx-background-color"));
        assertTrue(provider.requiredLabelStyle().contains("-fx-text-fill"));
    }

    @Test
    void entityCardStyles_keepSharedVisualContract() {
        assertTrue(provider.entityRowStyle(false, "rgba(1, 2, 3, 0.4)", "rgba(4, 5, 6, 0.4)")
                .contains("-fx-background-radius: 16"));
        assertTrue(provider.entityIconFrameStyle("rgba(1, 2, 3, 0.4)", "rgba(4, 5, 6, 0.4)")
                .contains("-fx-border-radius: 14"));
        assertTrue(provider.entityChipStyle("red", "blue", "white")
                .contains("-fx-background-radius: 999"));
        assertTrue(provider.formSectionStyle().contains("-fx-border-width: 0"));
        assertTrue(provider.iconPreviewStyle().contains("-fx-border-radius: 14"));
        assertTrue(provider.effectsBuilderStyle().contains("-fx-border-radius: 14"));
        assertTrue(provider.emptyStateStyle().contains("-fx-border-radius: 14"));
        assertTrue(provider.emptyStateTextStyle().contains("-fx-font-style"));
    }

    @Test
    void editorSurfacesUseAstralPaletteInsteadOfDetachedBlueGreen() {
        String combined = provider.inputCardStyle()
                + provider.listPanelStyle()
                + provider.formSectionStyle()
                + provider.effectsBuilderStyle()
                + provider.emptyStateStyle();

        assertTrue(combined.contains("rgba(75, 93, 127"));
        assertTrue(combined.contains("rgba(17, 23, 41")
                || combined.contains("rgba(18, 26, 48"));
        assertFalse(combined.contains("rgba(36, 59, 90"));
        assertFalse(combined.contains("rgba(124, 158, 195"));
    }
}













