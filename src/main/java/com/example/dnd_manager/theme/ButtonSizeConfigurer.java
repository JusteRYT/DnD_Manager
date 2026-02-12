package com.example.dnd_manager.theme;

import javafx.scene.control.Button;

/**
 * Utility class for enforcing fixed button size.
 * <p>
 * Ensures that buttons keep constant width and height
 * regardless of parent layout resizing.
 */
public final class ButtonSizeConfigurer {

    private ButtonSizeConfigurer() {
    }

    /**
     * Applies fixed size constraints to a button.
     *
     * @param button target button
     * @param width  fixed width
     * @param height fixed height
     */
    public static void applyFixedSize(Button button, double width, double height) {
        button.setMinSize(width, height);
        button.setPrefSize(width, height);
        button.setMaxSize(width, height);
    }
}
