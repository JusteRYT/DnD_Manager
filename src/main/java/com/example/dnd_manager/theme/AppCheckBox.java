package com.example.dnd_manager.theme;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

/**
 * Custom styled CheckBox for the DnD Manager theme.
 */
public class AppCheckBox extends HBox {

    @Getter
    private boolean selected;
    private final StackPane box = new StackPane();
    private final Label checkMark = new Label("✔");
    @Setter
    private Runnable onAction;

    public AppCheckBox(String text) {
        this.selected = false;
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setCursor(Cursor.HAND);

        // Квадрат чекбокса
        box.setMinSize(20, 20);
        box.setMaxSize(20, 20);
        box.setStyle("""
                -fx-background-color: #1a1a1a;
                -fx-border-color: #c89b3c;
                -fx-border-width: 2;
                -fx-border-radius: 4;
                -fx-background-radius: 4;
                """);

        // Галочка
        checkMark.setStyle("-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 14px;");
        checkMark.setVisible(false);
        box.getChildren().add(checkMark);

        // Текст рядом
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #e6e6e6; -fx-font-size: 13px;");

        getChildren().addAll(box, label);

        // Логика клика
        setOnMouseClicked(e -> {
            setSelected(!selected);
            if (onAction != null) onAction.run();
        });

        // Эффект наведения
        setOnMouseEntered(e -> box.setStyle(box.getStyle() + "-fx-border-color: #f5b741;"));
        setOnMouseExited(e -> box.setStyle(box.getStyle().replace("-fx-border-color: #f5b741;", "-fx-border-color: #c89b3c;")));
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        checkMark.setVisible(selected);
    }

}