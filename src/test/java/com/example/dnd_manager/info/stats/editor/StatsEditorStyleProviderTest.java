package com.example.dnd_manager.info.stats.editor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StatsEditorStyleProviderTest {

    private final StatsEditorStyleProvider provider = new StatsEditorStyleProvider();

    @Test
    void rowStyles_keepReadableCardContract() {
        assertTrue(provider.rowStyle(false).contains("-fx-background-radius: 12"));
        assertTrue(provider.rowStyle(true).contains("dropshadow"));
    }
}
