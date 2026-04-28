package com.example.dnd_manager.info.skills.popup;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillPopupPositionCalculatorTest {

    private final SkillPopupPositionCalculator calculator = new SkillPopupPositionCalculator();

    @Test
    void calculate_placesPopupToTheRightWhenThereIsSpace() {
        SkillPopupPosition position = calculator.calculate(200, 150, 50, 1000, 380, 8, 20);

        assertEquals(208, position.x());
        assertEquals(70, position.y());
    }

    @Test
    void calculate_flipsPopupToTheLeftWhenRightSideOverflows() {
        SkillPopupPosition position = calculator.calculate(850, 150, 420, 1000, 380, 8, 20);

        assertEquals(32, position.x());
        assertEquals(70, position.y());
    }

    @Test
    void calculate_clampsPopupTopToMinimumY() {
        SkillPopupPosition position = calculator.calculate(200, 40, 50, 1000, 380, 8, 20);

        assertEquals(208, position.x());
        assertEquals(20, position.y());
    }
}












