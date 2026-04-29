package com.example.dnd_manager.overview.dialogs.components;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.infrastructure.assets.CharacterAssetResolver;
import com.example.dnd_manager.theme.dialog.AppDialogStyleProvider;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class IconSlot extends StackPane {

    public IconSlot(IconSlotViewModel item, Character character) {
        setPrefSize(50, 50);
        setMinSize(50, 50);

        String color = item.getAccentColor();
        String baseStyle = slotStyle(color, false);
        String hoverStyle = slotStyle(color, true);
        setStyle(baseStyle);

        // Иконка
        ImageView iv = new ImageView();
        try {
            iv.setImage(CharacterAssetResolver.getImage(character, item.getIconPath(), 50, 50));
        } catch (Exception e) {
            Label placeholder = new Label(item.getName().substring(0, 1).toUpperCase());
            placeholder.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            getChildren().add(placeholder);
        }
        iv.setFitWidth(45);
        iv.setFitHeight(45);
        getChildren().add(iv);
        setupTooltip(item);

        setOnMouseEntered(e -> setStyle(hoverStyle));
        setOnMouseExited(e -> setStyle(baseStyle));
    }

    private String slotStyle(String color, boolean hover) {
        String background = hover ? "rgba(33, 45, 73, 0.90)" : "rgba(16, 23, 42, 0.86)";
        return """
                -fx-background-color: %s;
                -fx-background-radius: 8;
                -fx-border-color: %s;
                -fx-border-width: 1;
                -fx-border-radius: 8;
                -fx-cursor: hand;
                """.formatted(background, color);
    }

    private void setupTooltip(IconSlotViewModel item) {
        AppDialogStyleProvider styles = new AppDialogStyleProvider();
        VBox root = new VBox(5);
        root.setPadding(new Insets(10));
        root.setStyle(styles.panelStyle() + "-fx-border-color: " + item.getAccentColor() + "; -fx-min-width: 200; -fx-max-width: 300;");

        Label name = new Label(item.getName().toUpperCase());
        name.setStyle("-fx-text-fill: #f0f2f7; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label type = new Label(item.getTypeName());
        type.setStyle("-fx-text-fill: " + item.getAccentColor() + "; " +
                "-fx-font-size: 9px; " +
                "-fx-font-weight: bold;");

        root.getChildren().addAll(name, type, new Separator());

        item.getAttributes().forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                HBox row = new HBox(5);
                Label k = new Label(key + ":");
                k.setStyle("-fx-text-fill: #8fa4bd; -fx-font-size: 10px; -fx-font-weight: bold;");
                Label v = new Label(value);
                v.setStyle("-fx-text-fill: #dbe5ea; -fx-font-size: 10px;");
                v.setWrapText(true);
                row.getChildren().addAll(k, v);
                root.getChildren().add(row);
            }
        });

        if (item.getDescription() != null) {
            HBox row = new HBox(5);
            Label k = new Label(I18n.t("label.familiarsDescription") + ":");
            k.setStyle("-fx-text-fill: #8fa4bd; -fx-font-size: 10px; -fx-font-weight: bold;");
            Label desc = new Label(item.getDescription());
            desc.setWrapText(true);
            desc.setStyle("-fx-text-fill: #b7c9dd; -fx-font-size: 11px; -fx-font-style: italic;");
            row.getChildren().addAll(k, desc);
            root.getChildren().add(row);
        }

        Tooltip t = new Tooltip();
        t.setGraphic(root);
        t.setShowDelay(Duration.millis(100));
        t.setPrefHeight(200);
        t.setStyle("-fx-background-color: transparent;");
        Tooltip.install(this, t);
    }
}











