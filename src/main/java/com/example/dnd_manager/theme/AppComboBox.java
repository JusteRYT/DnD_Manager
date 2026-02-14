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
            -fx-background-color: #3a3a3a, #1e1e1e;
            -fx-background-insets: 0, 1;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            -fx-padding: 2 5 2 5; 
            -fx-font-size: 12px;
            -fx-text-fill: #eee;
            -fx-focus-color: transparent;
            -fx-faint-focus-color: transparent;
        """;

        setStyle(baseStyle);

        // Слушатель для золотой рамки
        focusedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                setStyle(baseStyle.replace("-fx-background-color: #3a3a3a, #1e1e1e;",
                        "-fx-background-color: #FFC107, #1e1e1e;")
                        + "-fx-effect: dropshadow(three-pass-box, rgba(255,193,7,0.1), 10, 0, 0, 0);");
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
                        setStyle("-fx-background-color: #1e1e1e;");
                    } else {
                        setText(item.toString());
                        setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #eee; -fx-padding: 8 12;");
                    }
                }
            };

            cell.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    ListView<?> listView = (ListView<?>) cell.getListView();
                    if (listView != null) {
                        listView.setStyle("""
                            -fx-background-color: #3a3a3a, #1e1e1e;
                            -fx-background-insets: 0, 1;
                            -fx-padding: 1;
                        """);
                    }
                }
            });

            cell.setOnMouseEntered(e -> {
                if (!cell.isEmpty()) cell.setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: #FFC107; -fx-padding: 8 12;");
            });
            cell.setOnMouseExited(e -> {
                if (!cell.isEmpty()) cell.setStyle("-fx-background-color: #1e1e1e; -fx-text-fill: #eee; -fx-padding: 8 12;");
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
                    setStyle("-fx-text-fill: #eee;");
                }
            }
        });
    }
}