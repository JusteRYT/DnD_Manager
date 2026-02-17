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
import lombok.Setter;

/**
 * Panel representing character's mana.
 * Uses safe coloring to avoid ClassCastException warnings in JavaFX 17.
 */
public class ManaBar extends VBox {

    private final Character target;
    private final Character owner;
    private final StorageService storageService;

    private final ProgressBar manaProgress = new ProgressBar();
    private final Label manaLabel = new Label();

    @Setter
    private Runnable onUpdate;

    public ManaBar(Character target, Character owner, StorageService storageService) {
        this.target = target;
        this.owner = owner;
        this.storageService = storageService;
        setSpacing(8);

        Label title = new Label(I18n.t("manaField.name.overview"));
        title.setStyle("""
            -fx-text-fill: #3aa3c3;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            """);

        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        // --- Настройка ProgressBar (Цвет полоски зафиксирован здесь) ---
        manaProgress.setMinWidth(100);
        manaProgress.setPrefWidth(150);
        manaProgress.setMaxWidth(Double.MAX_VALUE);

        // Этот стиль отвечает за внутренний акцент прогресс-бара
        manaProgress.setStyle("-fx-accent: #3aa3c3; -fx-control-inner-background: #1a1a1a;");

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

        var addBtn = AppButtonFactory.createValueAdjustButton(true, 28, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);
        addBtn.setOnAction(e -> changeMana(1));

        var removeBtn = AppButtonFactory.createValueAdjustButton(false, 28, AppTheme.BUTTON_REMOVE, AppTheme.BUTTON_REMOVE_HOVER);
        removeBtn.setOnAction(e -> changeMana(-1));

        HBox.setHgrow(manaProgress, Priority.ALWAYS);
        row.getChildren().addAll(manaProgress, manaLabel, addBtn, removeBtn);

        // --- ЛОГИКА ПОДСВЕТКИ КОНТЕЙНЕРА ---
        String accentColor = "#3aa3c3";
        String commonStyle = """
            -fx-border-color: %1$s;
            -fx-border-radius: 8;
            -fx-border-width: 1.2;
            -fx-background-radius: 8;
            -fx-background-color: linear-gradient(to bottom right, #252526, #1e1e1e);
            -fx-padding: 12;
            """.formatted(accentColor);

        String idleStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, rgba(58, 163, 195, 0.15), 10, 0, 0, 0);";
        String hoverStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, %s, 10, 0.2, 0, 0);".formatted(accentColor);

        // Устанавливаем стиль только для ManaBar (this)
        this.setStyle(idleStyle);

        this.setOnMouseEntered(e -> this.setStyle(hoverStyle));
        this.setOnMouseExited(e -> this.setStyle(idleStyle));

        getChildren().addAll(title, row);
        refresh();
    }

    /**
     * Changes current mana by delta. Current mana is clamped between 0 and maxMana.
     */
    private void changeMana(int delta) {
        int current = target.getCurrentMana();
        int max = Math.max(0, target.getMaxMana());
        int newMana = Math.max(0, Math.min(current + delta, max));

        target.setCurrentMana(newMana);
        refresh();

        storageService.saveCharacter(owner);

        if (onUpdate != null) {
            onUpdate.run();
        }
    }

    /**
     * Updates progress bar and label.
     */
    public void refresh() {
        int current = target.getCurrentMana();
        int max = Math.max(0, target.getMaxMana());

        manaProgress.setProgress(max > 0 ? (double) current / max : 0);
        manaLabel.setText(current + " / " + max);
    }

}
