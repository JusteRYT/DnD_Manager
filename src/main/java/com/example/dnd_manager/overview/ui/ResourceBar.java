package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Setter;

import java.util.Objects;

public class ResourceBar extends VBox {

    private final Character target;
    private final Character owner;
    private final SaveCharacterUseCase saveCharacterUseCase;
    private final CharacterResourceMetric metric;
    private final ResourceValueAdjuster valueAdjuster;
    private final int minDisplayedMax;

    private final ProgressBar progressBar = new ProgressBar();
    private final Label valueLabel = new Label();

    @Setter
    private Runnable onUpdate;

    public ResourceBar(
            Character target,
            Character owner,
            SaveCharacterUseCase saveCharacterUseCase,
            CharacterResourceMetric metric,
            ResourceValueAdjuster valueAdjuster,
            String titleKey,
            String accentColor,
            String idleShadowRgba,
            int minDisplayedMax,
            Double fixedLabelWidth
    ) {
        this.target = Objects.requireNonNull(target, "target must not be null");
        this.owner = Objects.requireNonNull(owner, "owner must not be null");
        this.saveCharacterUseCase = Objects.requireNonNull(saveCharacterUseCase, "saveCharacterUseCase must not be null");
        this.metric = Objects.requireNonNull(metric, "metric must not be null");
        this.valueAdjuster = Objects.requireNonNull(valueAdjuster, "valueAdjuster must not be null");
        this.minDisplayedMax = Math.max(0, minDisplayedMax);

        setSpacing(8);

        Label title = new Label(I18n.t(titleKey));
        title.setStyle("""
            -fx-text-fill: %s;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            """.formatted(accentColor));

        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        progressBar.setMinWidth(100);
        progressBar.setPrefWidth(150);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setStyle("-fx-accent: %s; -fx-control-inner-background: #1a1a1a;".formatted(accentColor));
        progressBar.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) {
                return;
            }
            progressBar.applyCss();
            Region track = (Region) progressBar.lookup(".track");
            if (track != null) {
                track.setBackground(new Background(new BackgroundFill(
                        Color.web(AppTheme.BACKGROUND_SECONDARY), new CornerRadii(6), null
                )));
            }
            Region bar = (Region) progressBar.lookup(".bar");
            if (bar != null) {
                bar.setBackground(new Background(new BackgroundFill(
                        Color.web(accentColor), new CornerRadii(6), null
                )));
            }
        });

        valueLabel.setStyle("""
            -fx-text-fill: #f2f2f2;
            -fx-font-weight: bold;
            -fx-font-family: "Consolas";
            -fx-font-size: 14px;
            """);
        if (fixedLabelWidth != null) {
            valueLabel.setMinWidth(fixedLabelWidth);
            valueLabel.setPrefWidth(fixedLabelWidth);
            valueLabel.setAlignment(Pos.CENTER);
        } else {
            valueLabel.setMinWidth(USE_PREF_SIZE);
        }

        var addBtn = AppButtonFactory.createValueAdjustButton(true, 28, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);
        addBtn.setOnAction(e -> changeResource(1));

        var removeBtn = AppButtonFactory.createValueAdjustButton(false, 28, AppTheme.BUTTON_REMOVE, AppTheme.BUTTON_REMOVE_HOVER);
        removeBtn.setOnAction(e -> changeResource(-1));

        HBox.setHgrow(progressBar, Priority.ALWAYS);
        row.getChildren().addAll(progressBar, valueLabel, addBtn, removeBtn);

        String commonStyle = """
            -fx-border-color: %1$s;
            -fx-border-radius: 8;
            -fx-border-width: 1.2;
            -fx-background-radius: 8;
            -fx-background-color: linear-gradient(to bottom right, #252526, #1e1e1e);
            -fx-padding: 12;
            """.formatted(accentColor);
        String idleStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, %s, 10, 0, 0, 0);".formatted(idleShadowRgba);
        String hoverStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, %s, 10, 0.2, 0, 0);".formatted(accentColor);

        setStyle(idleStyle);
        setOnMouseEntered(e -> setStyle(hoverStyle));
        setOnMouseExited(e -> setStyle(idleStyle));

        getChildren().addAll(title, row);
        refresh();
    }

    private void changeResource(int delta) {
        valueAdjuster.change(target, delta, metric);
        refresh();

        saveCharacterUseCase.execute(owner);
        if (onUpdate != null) {
            onUpdate.run();
        }
    }

    public void refresh() {
        int current = Math.max(0, metric.getCurrent(target));
        int rawMax = Math.max(0, metric.getMax(target));
        int shownMax = Math.max(minDisplayedMax, rawMax);
        progressBar.setProgress(shownMax > 0 ? (double) current / shownMax : 0);
        valueLabel.setText(current + " / " + shownMax);
    }
}

