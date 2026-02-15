package com.example.dnd_manager.theme;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WindowResizer {
    private final Stage stage;
    private final int margin;
    private Cursor cursor = Cursor.DEFAULT;
    private double lastMouseX, lastMouseY;

    public WindowResizer(Stage stage, int margin) {
        this.stage = stage;
        this.margin = margin;
    }

    public static void listen(Stage stage, int margin) {
        WindowResizer resizer = new WindowResizer(stage, margin);
        Scene scene = stage.getScene();

        scene.addEventFilter(MouseEvent.MOUSE_MOVED, resizer::handleMoved);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, resizer::handlePressed);
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, resizer::handleDragged);
    }

    private void handleMoved(MouseEvent event) {
        if (stage.isMaximized()) {
            stage.getScene().setCursor(Cursor.DEFAULT);
            return;
        }

        double x = event.getSceneX();
        double y = event.getSceneY();
        double w = stage.getScene().getWidth();
        double h = stage.getScene().getHeight();

        Cursor newCursor = Cursor.DEFAULT;

        boolean left = x <= margin;
        boolean right = x >= w - margin;
        boolean bottom = y >= h - margin;

        if (left && bottom) newCursor = Cursor.SW_RESIZE;
        else if (right && bottom) newCursor = Cursor.SE_RESIZE;
        else if (left) newCursor = Cursor.W_RESIZE;
        else if (right) newCursor = Cursor.E_RESIZE;
        else if (bottom) newCursor = Cursor.S_RESIZE;

        this.cursor = newCursor;
        stage.getScene().setCursor(newCursor);
    }

    private void handlePressed(MouseEvent event) {
        lastMouseX = event.getScreenX();
        lastMouseY = event.getScreenY();
        if (cursor != Cursor.DEFAULT) {
            event.consume();
        }
    }

    private void handleDragged(MouseEvent event) {
        if (cursor == Cursor.DEFAULT || stage.isMaximized()) return;

        double deltaX = event.getScreenX() - lastMouseX;
        double deltaY = event.getScreenY() - lastMouseY;

        double newW = stage.getWidth();
        double newH = stage.getHeight();
        double newX = stage.getX();

        if (cursor == Cursor.E_RESIZE || cursor == Cursor.SE_RESIZE) {
            newW = stage.getWidth() + deltaX;
        } else if (cursor == Cursor.W_RESIZE || cursor == Cursor.SW_RESIZE) {
            double potentialWidth = stage.getWidth() - deltaX;
            if (potentialWidth > stage.getMinWidth()) {
                newW = potentialWidth;
                newX = stage.getX() + deltaX;
            }
        }

        if (cursor == Cursor.S_RESIZE || cursor == Cursor.SW_RESIZE || cursor == Cursor.SE_RESIZE) {
            newH = stage.getHeight() + deltaY;
        }

        if (newW >= stage.getMinWidth()) {
            stage.setX(newX);
            stage.setWidth(newW);
        }
        if (newH >= stage.getMinHeight()) {
            stage.setHeight(newH);
        }

        lastMouseX = event.getScreenX();
        lastMouseY = event.getScreenY();

        event.consume();
    }
}