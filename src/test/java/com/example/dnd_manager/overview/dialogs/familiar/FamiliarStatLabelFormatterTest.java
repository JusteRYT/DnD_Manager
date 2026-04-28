package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.info.stats.model.StatEnum;
import com.example.dnd_manager.lang.I18n;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FamiliarStatLabelFormatterTest {

    private final FamiliarStatLabelFormatter formatter = new FamiliarStatLabelFormatter();

    @Test
    void shortLabel_returnsUppercasePrefix() {
        I18n.setLocale(Locale.ENGLISH);
        String expected = StatEnum.STRANGE.getName().substring(0, 3).toUpperCase(Locale.ROOT);
        String label = formatter.shortLabel(StatEnum.STRANGE);
        assertEquals(expected, label);
    }
}












