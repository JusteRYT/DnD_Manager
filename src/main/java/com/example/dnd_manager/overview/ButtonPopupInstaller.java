package com.example.dnd_manager.overview;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Popup;

/**
 * Utility class responsible for installing popup behavior on JavaFX buttons.
 * <p>
 * This class follows the Single Responsibility Principle (SRP).
 * It encapsulates popup creation and hover behavior logic.
 */
public final class ButtonPopupInstaller {

    private ButtonPopupInstaller() {
        // Utility class
    }

    /**
     * Installs a popup with custom content on the specified button.
     *
     * @param button  the target button
     * @param content the content to show inside popup
     */
    public static void install(Button button, Node content) {
        Popup popup = new Popup();
        popup.getContent().add(content);
        popup.setAutoHide(false);

        button.setOnMouseEntered(e -> showPopup(button, popup));
        button.setOnMouseExited(e -> popup.hide());
    }

    private static void showPopup(Button button, Popup popup) {
        Bounds bounds = button.localToScreen(button.getBoundsInLocal());
        popup.show(button.getScene().getWindow(),
                bounds.getMaxX() + 5,
                bounds.getMinY());
    }
}

