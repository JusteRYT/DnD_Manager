package com.example.dnd_manager.info.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseInfoFormStyleProviderTest {

    private final BaseInfoFormStyleProvider provider = new BaseInfoFormStyleProvider();

    @Test
    void styleTokens_areProvided() {
        assertTrue(provider.sectionTitleStyle().contains("-fx-letter-spacing"));
        assertTrue(provider.fieldLabelStyle().contains("#c89b3c"));
        assertTrue(provider.requiredLabelStyle().contains("#ff6b6b"));
    }
}












