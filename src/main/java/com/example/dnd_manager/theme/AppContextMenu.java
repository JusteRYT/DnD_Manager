package com.example.dnd_manager.theme;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class AppContextMenu extends ContextMenu {

    private static final String BG_COLOR = "#2b2b2b";
    private static final String BORDER_COLOR = "#3a3a3a";
    private static final String HOVER_BG_COLOR = "#3d3d3d";
    private static final String ACCENT_COLOR = "#c89b3c"; // Твой золотой
    private static final String DANGER_COLOR = "#ff6b6b"; // Твой красный

    public AppContextMenu() {
        this.setStyle(String.format("""
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-border-width: 1;
                -fx-background-insets: 0;
                """, BG_COLOR, BORDER_COLOR));

        this.setOnShowing(e -> {
            if (this.getScene() != null && this.getScene().getRoot() != null) {
                this.getScene().getRoot().setStyle("-fx-background-color: transparent;");
                this.getScene().getRoot().lookupAll(".menu-item").forEach(n -> {
                    n.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
                });
            }
        });
    }

    public void addActionItem(String text, Runnable action) {
        // Передаем ACCENT_COLOR как основной и как hover
        this.getItems().add(createStyledItem(text, action, ACCENT_COLOR, ACCENT_COLOR));
    }

    public void addDeleteItem(String text, Runnable action) {
        // Передаем DANGER_COLOR как основной и как hover
        this.getItems().add(createStyledItem(text, action, DANGER_COLOR, DANGER_COLOR));
    }

    private CustomMenuItem createStyledItem(String text, Runnable action, String normalColor, String hoverColor) {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPadding(new Insets(8, 20, 8, 20));

        String fontStyle = "-fx-font-weight: bold; -fx-font-size: 13px;";

        label.setStyle("-fx-text-fill: " + normalColor + "; " + fontStyle);

        StackPane container = new StackPane(label);
        container.setPrefWidth(160);
        container.setFocusTraversable(false);
        container.setStyle("-fx-background-color: transparent;");

        container.setOnMouseEntered(e -> {
            container.setStyle("-fx-background-color: " + HOVER_BG_COLOR + ";");
            label.setStyle("-fx-text-fill: " + hoverColor + "; " + fontStyle);
        });

        container.setOnMouseExited(e -> {
            container.setStyle("-fx-background-color: transparent;");
            label.setStyle("-fx-text-fill: " + normalColor + "; " + fontStyle);
        });

        CustomMenuItem customItem = new CustomMenuItem(container);
        customItem.setHideOnClick(true);
        customItem.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        customItem.setOnAction(ev -> action.run());

        return customItem;
    }
}