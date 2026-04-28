package com.example.dnd_manager.info.editors.inventory;

import com.example.dnd_manager.lang.I18n;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InventoryEditorEffectsSummaryFormatterTest {

    private final InventoryEditorEffectsSummaryFormatter formatter = new InventoryEditorEffectsSummaryFormatter();

    @Test
    void emptyText_usesLocalization() {
        I18n.setLocale(Locale.ENGLISH);

        assertEquals("No effects attached", formatter.emptyText());
    }

    @Test
    void format_clampsNegativeCountsAndFormatsValues() {
        I18n.setLocale(Locale.ENGLISH);

        String text = formatter.format(-1, 3);

        assertTrue(text.contains("0 buffs"));
        assertTrue(text.contains("3 skills"));
    }
}













