package com.example.dnd_manager.info.editors.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EntityEditorStyleProviderTest {

    private final EntityEditorStyleProvider provider = new EntityEditorStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.titleStyle().contains("-fx-font-size"));
        assertTrue(provider.inputCardStyle().contains("-fx-background-color"));
        assertTrue(provider.requiredLabelStyle().contains("-fx-text-fill"));
    }
}













