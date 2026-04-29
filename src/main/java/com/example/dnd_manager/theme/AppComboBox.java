package com.example.dnd_manager.theme;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class AppComboBox<T> extends ComboBox<T> {

    public AppComboBox() {
        super();
        applyStyle();
        setPrefHeight(41);
    }

    private void applyStyle() {
        String baseStyle = """
            -fx-background-color: rgba(75, 93, 127, 0.42), rgba(16, 23, 42, 0.88);
            -fx-background-insets: 0, 1;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            -fx-padding: 2 5 2 5;
            -fx-font-size: 12px;
            -fx-text-fill: #f0f2f7;
            -fx-focus-color: transparent;
            -fx-faint-focus-color: transparent;
        """;
        String focusStyle = """
            -fx-background-color: rgba(175, 196, 216, 0.62), rgba(16, 23, 42, 0.88);
            -fx-background-insets: 0, 1;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            -fx-padding: 2 5 2 5;
            -fx-font-size: 12px;
            -fx-text-fill: #f0f2f7;
            -fx-focus-color: transparent;
            -fx-faint-focus-color: transparent;
            -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.18), 12, 0.24, 0, 0);
        """;

        setStyle(baseStyle);

        focusedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                setStyle(focusStyle);
            } else {
                setStyle(baseStyle);
            }
        });

        setCellFactory(lv -> {
            ListCell<T> cell = new ListCell<>() {
                @Override
                protected void updateItem(T item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("-fx-background-color: #10172a;");
                    } else {
                        setText(item.toString());
                        setStyle("-fx-background-color: #10172a; -fx-text-fill: #f0f2f7; -fx-padding: 8 12;");
                    }
                }
            };

            cell.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    ListView<T> listView = cell.getListView();
                    if (listView != null) {
                        listView.setStyle("""
                            -fx-background-color: rgba(75, 93, 127, 0.42), #10172a;
                            -fx-background-insets: 0, 1;
                            -fx-padding: 1;
                        """);
                    }
                }
            });

            cell.setOnMouseEntered(e -> {
                if (!cell.isEmpty()) cell.setStyle("-fx-background-color: rgba(39, 47, 79, 0.86); -fx-text-fill: #dbe5ea; -fx-padding: 8 12;");
            });
            cell.setOnMouseExited(e -> {
                if (!cell.isEmpty()) cell.setStyle("-fx-background-color: #10172a; -fx-text-fill: #f0f2f7; -fx-padding: 8 12;");
            });

            return cell;
        });

        setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    setText(item.toString());
                    setStyle("-fx-text-fill: #f0f2f7;");
                }
            }
        });
    }
}











