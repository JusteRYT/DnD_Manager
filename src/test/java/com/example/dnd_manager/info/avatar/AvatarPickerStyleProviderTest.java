package com.example.dnd_manager.info.avatar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AvatarPickerStyleProviderTest {

    private final AvatarPickerStyleProvider provider = new AvatarPickerStyleProvider();

    @Test
    void styles_keepPortraitCardContract() {
        assertTrue(provider.frameStyle().contains("-fx-border-radius: 20"));
        assertTrue(provider.actionButtonStyle(false).contains("-fx-background-radius: 8"));
        assertTrue(provider.actionButtonStyle(true).contains("dropshadow"));
    }
}
