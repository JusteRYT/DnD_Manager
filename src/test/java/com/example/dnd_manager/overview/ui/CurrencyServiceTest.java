package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyServiceTest {

    private final CurrencyService service = new CurrencyService();

    @Test
    void adjust_doesNotAllowNegativeTotal() {
        Character character = new Character();
        character.setTotalCooper(3);

        service.adjust(character, -10);

        assertEquals(0, character.getTotalCooper());
    }

    @Test
    void toDisplayValues_mapsCooperToCoinLabels() {
        CurrencyDisplayValues values = service.toDisplayValues(27);

        assertEquals(2, values.gold());
        assertEquals(5, values.silver());
        assertEquals(27, values.copper());
    }
}

