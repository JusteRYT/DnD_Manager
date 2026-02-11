package com.example.dnd_manager.overview;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lombok.Getter;

import java.util.Objects;

/**
 * Panel to display and modify character's inspiration and mana points.
 * Includes inspiration value and mana bar with current/max values.
 * Saves character automatically on change.
 */
public class InspirationPanel extends VBox {

    private final Character character;
    private final StorageService storageService;

    private final Label inspirationLabel = new Label();
    @Getter
    private final ManaBar manaBar;

    public InspirationPanel(Character character, StorageService storageService) {
        this.character = character;
        this.storageService = storageService;

        setSpacing(8);
        setPadding(new Insets(8));
        setStyle("""
                -fx-border-color: #3a3a3a;
                -fx-border-radius: 6;
                -fx-border-width: 1;
                -fx-background-radius: 6;
                -fx-background-color: #252526;
                """);

        // --- Inspiration section ---
        Label title = new Label("Inspiration");
        title.setStyle("""
                -fx-text-fill: #c89b3c;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                """);

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        ImageView icon = new ImageView(
                new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/com/example/dnd_manager/icon/icon_heart.png")
                ))
        );
        icon.setFitWidth(26);
        icon.setFitHeight(26);
        icon.setPreserveRatio(true);

        inspirationLabel.setStyle("""
                        -fx-text-fill: #f2f2f2;
                        -fx-font-weight: bold;
                        -fx-font-family: "Consolas";
                        -fx-font-size: 14px;
                """);

        StackPane valueBox = new StackPane(inspirationLabel);
        valueBox.setMinWidth(50);
        valueBox.setPrefWidth(50);
        valueBox.setMaxWidth(50);
        valueBox.setMinHeight(28);
        valueBox.setPrefHeight(28);
        valueBox.setAlignment(Pos.CENTER);   // центрируем текст
        valueBox.setStyle("""
                        -fx-background-color: #1e1e1e;
                        -fx-background-radius: 6;
                        -fx-padding: 2 8;
                """);

        // Кнопки без влияния на ширину valueBox
        var addBtn = AppButtonFactory.customButton("+", 28);
        addBtn.setOnAction(e -> changeInspiration(1));

        var removeBtn = AppButtonFactory.customButton("–", 28,
                AppTheme.BUTTON_REMOVE, AppTheme.BUTTON_REMOVE_HOVER);
        removeBtn.setOnAction(e -> changeInspiration(-1));

        row.getChildren().addAll(icon, valueBox, addBtn, removeBtn);
        HBox.setHgrow(valueBox, Priority.ALWAYS);

        getChildren().addAll(title, row);

        // --- Mana section ---
        manaBar = new ManaBar(character, storageService);
        getChildren().add(manaBar);

        refresh();
    }

    private void changeInspiration(int delta) {
        character.setInspiration(Math.max(0, character.getInspiration() + delta));
        refresh();
        storageService.saveCharacter(character);
    }

    public void refresh() {
        inspirationLabel.setText(String.valueOf(character.getInspiration()));
        manaBar.refresh();
    }

}
