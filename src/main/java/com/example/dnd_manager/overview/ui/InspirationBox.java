package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class InspirationBox extends VBox {
    private final Label valLabel = new Label();

    public InspirationBox(Character character, StorageService storageService) {
        setSpacing(8);
        setPadding(new Insets(12));

        Label title = new Label(I18n.t("inspirationPanel.title"));
        title.setStyle("""
                -fx-text-fill: #dba1ff;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.8), 2, 0, 0, 1);
                """);

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        ImageView icon = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/com/example/dnd_manager/icon/inspiration_icon.png")
                ))
        );
        icon.setFitWidth(28);
        icon.setFitHeight(28);
        icon.setPreserveRatio(true);
        icon.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(155, 89, 182, 0.6), 5, 0, 0, 0);");

        valLabel.setStyle("""
                        -fx-text-fill: #ffffff;
                        -fx-font-weight: bold;
                        -fx-font-family: "Consolas";
                        -fx-font-size: 18px;
                """);

        StackPane valContainer = new StackPane(valLabel);
        valContainer.setStyle("-fx-background-color: #1a1a1a; -fx-background-radius: 5; -fx-min-width: 40;");

        var add = AppButtonFactory.customButton("+", 24);
        add.setOnAction(e -> {
            character.setInspiration(character.getInspiration() + 1);
            storageService.saveCharacter(character);
            valLabel.setText(String.valueOf(character.getInspiration()));
        });

        row.getChildren().addAll(icon, valContainer, add);

        setStyle("""
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
                -fx-background-radius: 8;
                -fx-border-color: #9b59b6;
                -fx-border-radius: 8;
                -fx-effect: dropshadow(three-pass-box, rgba(155, 89, 182, 0.2), 10, 0, 0, 0);
                """);

        getChildren().addAll(title, row);
        valLabel.setText(String.valueOf(character.getInspiration()));
    }
}