package com.example.dnd_manager.assets.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetGalleryTabStyleProviderTest {

    private final AssetGalleryTabStyleProvider provider = new AssetGalleryTabStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.rootStyle().contains("transparent"));
        assertTrue(provider.scrollPaneStyle().contains("-fx-control-inner-background"));
    }
}












