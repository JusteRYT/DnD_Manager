package com.example.dnd_manager.info.editors.familiar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FamiliarEditorStyleProviderTest {

    private final FamiliarEditorStyleProvider provider = new FamiliarEditorStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.baseCardStyle().contains("#252526"));
        assertTrue(provider.fieldLabelStyle().contains("-fx-font-size"));
    }
}












