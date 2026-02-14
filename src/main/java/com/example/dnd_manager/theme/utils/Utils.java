package com.example.dnd_manager.theme.utils;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class Utils {
    /**
     * Вспомогательный метод для создания жирных иконок + и -
     */
    public static StackPane createAdjustIcon(boolean isPlus, int btnSize) {
        double thickness = 3.0; // Толщина линий (регулируй для жирности)
        double length = btnSize * 0.45; // Длина линий относительно размера кнопки

        javafx.scene.shape.Rectangle horizontal = new javafx.scene.shape.Rectangle(length, thickness);
        horizontal.setFill(javafx.scene.paint.Color.WHITE);
        horizontal.setArcWidth(2);
        horizontal.setArcHeight(2);

        StackPane iconPane = new StackPane(horizontal);

        if (isPlus) {
            javafx.scene.shape.Rectangle vertical = new javafx.scene.shape.Rectangle(thickness, length);
            vertical.setFill(javafx.scene.paint.Color.WHITE);
            vertical.setArcWidth(2);
            vertical.setArcHeight(2);
            iconPane.getChildren().add(vertical);
        }

        iconPane.setAlignment(Pos.CENTER);
        return iconPane;
    }
}
