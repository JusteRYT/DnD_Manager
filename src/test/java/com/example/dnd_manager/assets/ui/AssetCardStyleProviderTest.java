package com.example.dnd_manager.assets.ui;

import com.example.dnd_manager.theme.AppTheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetCardStyleProviderTest {

    private final AssetCardStyleProvider provider = new AssetCardStyleProvider();

    @Test
    void cardStyle_reflectsSelectionState() {
        assertTrue(provider.cardStyle(false).contains("#2b2b2b"));
        assertTrue(provider.cardStyle(false).contains("transparent"));
        assertTrue(provider.cardStyle(true).contains("#404040"));
        assertTrue(provider.cardStyle(true).contains(AppTheme.TEXT_ACCENT));
    }

    @Test
    void fileNameLabelStyle_containsTextTokens() {
        assertTrue(provider.fileNameLabelStyle().contains("-fx-text-fill"));
        assertTrue(provider.fileNameLabelStyle().contains("11px"));
    }
}












