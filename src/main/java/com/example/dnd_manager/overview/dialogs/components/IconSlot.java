package com.example.dnd_manager.overview.dialogs.components;

import com.example.dnd_manager.info.familiar.Displayable;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class IconSlot extends StackPane {

    public IconSlot(Displayable item, String characterName) {
        setPrefSize(50, 50);
        setMinSize(50, 50);

        String color = item.getAccentColor();
        setStyle(String.format("-fx-background-color: #2b2b2b; -fx-background-radius: 6; -fx-border-color: %s; -fx-border-width: 1; -fx-border-radius: 6; -fx-cursor: hand;", color));

        // Иконка
        ImageView iv = new ImageView();
        try {
            iv.setImage(new Image(CharacterAssetResolver.resolve(characterName, item.getIconPath())));
        } catch (Exception e) {
            Label placeholder = new Label(item.getName().substring(0, 1).toUpperCase());
            placeholder.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            getChildren().add(placeholder);
        }
        iv.setFitWidth(45); iv.setFitHeight(45);
        getChildren().add(iv);

        if (item.getBadgeText() != null) {
            Label badge = new Label(item.getBadgeText());
            badge.setStyle("-fx-text-fill: white; " +
                    "-fx-font-size: 9px; " +
                    "-fx-background-color: rgba(0,0,0,0.7); " +
                    "-fx-background-radius: 3; " +
                    "-fx-padding: 1 3;");
            StackPane.setAlignment(badge, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(badge, new Insets(2));
            getChildren().add(badge);
        }

        setupTooltip(item);

        setOnMouseEntered(e -> setStyle(getStyle() + "-fx-background-color: #3d3d3d;"));
        setOnMouseExited(e -> setStyle(getStyle() + "-fx-background-color: #2b2b2b;"));
    }

    private void setupTooltip(Displayable item) {
        VBox root = new VBox(5);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #1a1a1a; " +
                "-fx-border-color: " + item.getAccentColor() + "; -fx-border-width: 1; " +
                "-fx-min-width: 200; -fx-max-width: 300;");

        Label name = new Label(item.getName().toUpperCase());
        name.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label type = new Label(item.getTypeName());
        type.setStyle("-fx-text-fill: " + item.getAccentColor() + "; " +
                "-fx-font-size: 9px; " +
                "-fx-font-weight: bold;");

        root.getChildren().addAll(name, type, new Separator());

        // Отрисовываем все уникальные поля, которые есть у сущности
        item.getAttributes().forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                HBox row = new HBox(5);
                Label k = new Label(key + ":");
                k.setStyle("-fx-text-fill: #888; -fx-font-size: 10px; -fx-font-weight: bold;");
                Label v = new Label(value);
                v.setStyle("-fx-text-fill: #eee; -fx-font-size: 10px;");
                v.setWrapText(true);
                row.getChildren().addAll(k, v);
                root.getChildren().add(row);
            }
        });

        if (item.getDescription() != null) {
            HBox row = new HBox(5);
            Label k = new Label(I18n.t("label.familiarsDescription") + ":");
            k.setStyle("-fx-text-fill: #888; -fx-font-size: 10px; -fx-font-weight: bold;");
            Label desc = new Label(item.getDescription());
            desc.setWrapText(true);
            desc.setStyle("-fx-text-fill: #bbb; -fx-font-size: 11px; -fx-font-style: italic;");
            row.getChildren().addAll(k, desc);
            root.getChildren().add(row);
        }

        Tooltip t = new Tooltip();
        t.setGraphic(root);
        t.setShowDelay(Duration.millis(100));
        t.setStyle("-fx-background-color: transparent;");
        Tooltip.install(this, t);
    }
}