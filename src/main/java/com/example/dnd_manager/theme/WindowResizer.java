package com.example.dnd_manager.theme;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowResizer {

    private final Stage stage;
    private final int margin;

    // Начальные координаты мыши в момент клика
    private double startMouseX;
    private double startMouseY;

    // Начальные параметры окна в момент клика
    private double startX;
    private double startY;
    private double startW;
    private double startH;

    private boolean resizing = false;
    private Cursor activeResizeCursor = Cursor.DEFAULT;

    public WindowResizer(Stage stage, int margin) {
        this.stage = stage;
        this.margin = margin;
    }

    public static void listen(Stage stage, int margin) {
        WindowResizer resizer = new WindowResizer(stage, margin);
        Scene scene = stage.getScene();

        scene.addEventFilter(MouseEvent.MOUSE_MOVED, resizer::handleMoved);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, resizer::handlePressed);
        scene.addEventFilter(MouseEvent.MOUSE_RELEASED, resizer::handleReleased);
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, resizer::handleDragged);
    }

    private void handleMoved(MouseEvent event) {
        if (stage.isMaximized()) {
            stage.getScene().setCursor(Cursor.DEFAULT);
            return;
        }

        double x = event.getSceneX();
        double y = event.getSceneY();
        double w = stage.getWidth();
        double h = stage.getHeight();

        Cursor newCursor = Cursor.DEFAULT;

        boolean left = x <= margin;
        boolean right = x >= w - margin;
        boolean bottom = y >= h - margin;

        if (left && bottom) newCursor = Cursor.SW_RESIZE;
        else if (right && bottom) newCursor = Cursor.SE_RESIZE;
        else if (left) newCursor = Cursor.W_RESIZE;
        else if (right) newCursor = Cursor.E_RESIZE;
        else if (bottom) newCursor = Cursor.S_RESIZE;

        if (!resizing) {
            stage.getScene().setCursor(newCursor);
        }
    }

    private void handlePressed(MouseEvent event) {
        // Запоминаем начальные координаты
        startMouseX = event.getScreenX();
        startMouseY = event.getScreenY();

        // Запоминаем начальное состояние окна
        startX = stage.getX();
        startY = stage.getY();
        startW = stage.getWidth();
        startH = stage.getHeight();

        double x = event.getSceneX();
        double y = event.getSceneY();
        double w = stage.getWidth();
        double h = stage.getHeight();

        boolean left = x <= margin;
        boolean right = x >= w - margin;
        boolean bottom = y >= h - margin;

        if (left && bottom) activeResizeCursor = Cursor.SW_RESIZE;
        else if (right && bottom) activeResizeCursor = Cursor.SE_RESIZE;
        else if (left) activeResizeCursor = Cursor.W_RESIZE;
        else if (right) activeResizeCursor = Cursor.E_RESIZE;
        else if (bottom) activeResizeCursor = Cursor.S_RESIZE;
        else activeResizeCursor = Cursor.DEFAULT;

        resizing = activeResizeCursor != Cursor.DEFAULT;
        if (resizing) event.consume();
    }

    private void handleReleased(MouseEvent event) {
        resizing = false;
        activeResizeCursor = Cursor.DEFAULT;
    }

    private void handleDragged(MouseEvent event) {
        if (!resizing || stage.isMaximized()) return;

        // Считаем общее смещение от точки нажатия
        double totalDeltaX = event.getScreenX() - startMouseX;
        double totalDeltaY = event.getScreenY() - startMouseY;

        // Ресайз вправо
        if (activeResizeCursor == Cursor.E_RESIZE || activeResizeCursor == Cursor.SE_RESIZE) {
            stage.setWidth(Math.max(startW + totalDeltaX, stage.getMinWidth()));
        }
        // Ресайз влево (меняется и ширина, и X координата)
        else if (activeResizeCursor == Cursor.W_RESIZE || activeResizeCursor == Cursor.SW_RESIZE) {
            double maxDeltaX = startW - stage.getMinWidth();
            double clampedDeltaX = Math.min(totalDeltaX, maxDeltaX);

            stage.setWidth(startW - clampedDeltaX);
            stage.setX(startX + clampedDeltaX);
        }

        // Вертикальный ресайз (низ)
        if (activeResizeCursor == Cursor.S_RESIZE || activeResizeCursor == Cursor.SE_RESIZE || activeResizeCursor == Cursor.SW_RESIZE) {
            stage.setHeight(Math.max(startH + totalDeltaY, stage.getMinHeight()));
        }

        event.consume();
    }
}