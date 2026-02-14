package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Panel representing character's mana.
 * Uses safe coloring to avoid ClassCastException warnings in JavaFX 17.
 */
public class ManaBar extends VBox {

    private final Character character;
    private final StorageService storageService;

    private final ProgressBar manaProgress = new ProgressBar();
    private final Label manaLabel = new Label();

    public ManaBar(Character character, StorageService storageService) {
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

        Label title = new Label(I18n.t("manaField.name.overview"));
        title.setStyle("""
                -fx-text-fill: #3aa3c3;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                """);

        // Row with progress bar and value
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        manaProgress.setMinWidth(100);
        manaProgress.setPrefWidth(150);
        manaProgress.setMaxWidth(Double.MAX_VALUE);
        manaProgress.setProgress(0);

        manaProgress.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                manaProgress.applyCss();

                Region track = (Region) manaProgress.lookup(".track");
                if (track != null) {
                    track.setBackground(new Background(new BackgroundFill(
                            Color.web(AppTheme.BACKGROUND_SECONDARY), new CornerRadii(6), null
                    )));
                }

                Region bar = (Region) manaProgress.lookup(".bar");
                if (bar != null) {
                    bar.setBackground(new Background(new BackgroundFill(
                            Color.web("#3aa3c3"), new CornerRadii(6), null
                    )));
                }
            }
        });

        manaLabel.setStyle("""
                -fx-text-fill: #f2f2f2;
                -fx-font-weight: bold;
                -fx-font-family: "Consolas";
                -fx-font-size: 14px;
                """);

        manaProgress.setStyle("-fx-accent: #3aa3c3; -fx-control-inner-background: #1a1a1a; -fx-text-box-border: transparent;");

        setStyle("""
                -fx-border-color: #3aa3c3;
                -fx-border-radius: 6;
                -fx-border-width: 1;
                -fx-background-radius: 6;
                -fx-background-color: linear-gradient(to bottom, #252526, #1e1e1e);
                -fx-effect: dropshadow(three-pass-box, rgba(58, 163, 195, 0.15), 10, 0, 0, 0);
                -fx-padding: 12;
                """);

        var addBtn = AppButtonFactory.createValueAdjustButton(true, 28, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);
        addBtn.setOnAction(e -> changeMana(1));

        var removeBtn = AppButtonFactory.createValueAdjustButton(false, 28,
                AppTheme.BUTTON_REMOVE, AppTheme.BUTTON_REMOVE_HOVER);
        removeBtn.setOnAction(e -> changeMana(-1));

        HBox.setHgrow(manaProgress, Priority.ALWAYS);
        row.getChildren().addAll(manaProgress, manaLabel, addBtn, removeBtn);

        getChildren().addAll(title, row);

        refresh();
    }

    /**
     * Changes current mana by delta. Current mana is clamped between 0 and maxMana.
     */
    private void changeMana(int delta) {
        int current = parseOrZero(character.getCurrentMana());
        int max = Math.max(0, parseOrZero(character.getMaxMana()));

        int newMana = Math.max(0, Math.min(current + delta, max));

        character.setCurrentMana(String.valueOf(newMana));
        refresh();
        storageService.saveCharacter(character);
    }

    /**
     * Safe integer parsing, returns 0 if invalid
     */
    private int parseOrZero(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Updates progress bar and label.
     */
    public void refresh() {
        int current = parseOrZero(character.getCurrentMana());
        int max = Math.max(0, parseOrZero(character.getMaxMana()));
        manaProgress.setProgress((double) current / max);
        manaLabel.setText(current + " / " + max);
    }

}
