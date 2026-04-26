package com.example.dnd_manager.updater;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateProgressCalculatorTest {

    private final UpdateProgressCalculator calculator = new UpdateProgressCalculator();

    @Test
    void calculate_returnsZeroWhenTotalIsZero() {
        assertEquals(0.0, calculator.calculate(10, 0));
    }

    @Test
    void calculate_returnsRatioInsideBounds() {
        assertEquals(0.5, calculator.calculate(50, 100));
    }

    @Test
    void calculate_clampsToOneWhenDownloadedExceedsTotal() {
        assertEquals(1.0, calculator.calculate(150, 100));
    }

    @Test
    void calculate_clampsNegativeValuesToZero() {
        assertEquals(0.0, calculator.calculate(-10, 100));
    }
}
