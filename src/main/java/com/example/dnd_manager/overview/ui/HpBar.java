package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class HpBar extends VBox {

    private final Character character;
    private final StorageService storageService;

    private final ProgressBar hpProgress = new ProgressBar();
    private final Label hpLabel = new Label();

    // Красный цвет для HP
    private static final String ACCENT_COLOR = "#ff4444";

    public HpBar(Character character, StorageService storageService) {
        this.character = character;
        this.storageService = storageService;

        setSpacing(8);

        Label title = new Label(I18n.t("label.familiarsHP"));
        title.setStyle(String.format("""
            -fx-text-fill: %s;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            """, ACCENT_COLOR));

        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        // --- Настройка ProgressBar ---
        hpProgress.setMinWidth(100);
        hpProgress.setPrefWidth(150);
        hpProgress.setMaxWidth(Double.MAX_VALUE);

        // CSS для красного цвета
        hpProgress.setStyle(String.format("-fx-accent: %s; -fx-control-inner-background: #1a1a1a;", ACCENT_COLOR));

        hpProgress.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                hpProgress.applyCss();
                Region track = (Region) hpProgress.lookup(".track");
                if (track != null) {
                    track.setBackground(new Background(new BackgroundFill(
                            Color.web(AppTheme.BACKGROUND_SECONDARY), new CornerRadii(6), null
                    )));
                }
                Region bar = (Region) hpProgress.lookup(".bar");
                if (bar != null) {
                    bar.setBackground(new Background(new BackgroundFill(
                            Color.web(ACCENT_COLOR), new CornerRadii(6), null
                    )));
                }
            }
        });

        hpLabel.setStyle("""
            -fx-text-fill: #f2f2f2;
            -fx-font-weight: bold;
            -fx-font-family: "Consolas";
            -fx-font-size: 14px;
            """);

        // Кнопки +/-
        var addBtn = AppButtonFactory.createValueAdjustButton(true, 28, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);
        addBtn.setOnAction(e -> changeHp(1));

        var removeBtn = AppButtonFactory.createValueAdjustButton(false, 28, AppTheme.BUTTON_REMOVE, AppTheme.BUTTON_REMOVE_HOVER);
        removeBtn.setOnAction(e -> changeHp(-1));

        HBox.setHgrow(hpProgress, Priority.ALWAYS);
        row.getChildren().addAll(hpProgress, hpLabel, addBtn, removeBtn);

        // --- Стилизация контейнера (Красное свечение) ---
        String commonStyle = """
            -fx-border-color: %1$s;
            -fx-border-radius: 8;
            -fx-border-width: 1.2;
            -fx-background-radius: 8;
            -fx-background-color: linear-gradient(to bottom right, #252526, #1e1e1e);
            -fx-padding: 12;
            """.formatted(ACCENT_COLOR);

        String idleStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, rgba(255, 68, 68, 0.15), 10, 0, 0, 0);";
        String hoverStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, %s, 10, 0.2, 0, 0);".formatted(ACCENT_COLOR);

        this.setStyle(idleStyle);
        this.setOnMouseEntered(e -> this.setStyle(hoverStyle));
        this.setOnMouseExited(e -> this.setStyle(idleStyle));

        getChildren().addAll(title, row);
        refresh();
    }

    private void changeHp(int delta) {
        int current = character.getCurrentHp();
        int max = character.getMaxHp();

        int newVal = Math.max(0, Math.min(current + delta, max));

        character.setCurrentHp(newVal);
        refresh();
        storageService.saveCharacter(character);
    }

    public void refresh() {
        int current = character.getCurrentHp();
        int max = Math.max(1, character.getMaxHp());
        hpProgress.setProgress((double) current / max);
        hpLabel.setText(current + " / " + max);
        hpLabel.setMinWidth(USE_PREF_SIZE);
    }
}