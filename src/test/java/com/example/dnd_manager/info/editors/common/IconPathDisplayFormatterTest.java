package com.example.dnd_manager.info.editors.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IconPathDisplayFormatterTest {

    private final IconPathDisplayFormatter formatter = new IconPathDisplayFormatter();

    @Test
    void fileNameOrEmpty_returnsEmptyForBlankPath() {
        assertEquals("", formatter.fileNameOrEmpty(null));
        assertEquals("", formatter.fileNameOrEmpty(""));
        assertEquals("", formatter.fileNameOrEmpty("   "));
    }

    @Test
    void fileNameOrFallback_returnsFallbackForMissingPath() {
        assertEquals("No icon", formatter.fileNameOrFallback(null, "No icon"));
    }

    @Test
    void fileNameOrEmpty_extractsFileName() {
        assertEquals("sword.png", formatter.fileNameOrEmpty("Assets/Items/sword.png"));
    }
}












