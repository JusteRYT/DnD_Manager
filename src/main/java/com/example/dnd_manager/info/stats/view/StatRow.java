package com.example.dnd_manager.info.stats.view;

import com.example.dnd_manager.info.stats.model.StatEnum;
import com.example.dnd_manager.info.editors.common.EntityEditorButtonFactory;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * UI component representing a single character stat row with + and - buttons.
 */
public class StatRow extends HBox {

    private final Label valueLabel;
    private final Button increaseButton;
    private final Button decreaseButton;

    public StatRow(StatEnum statName, int initialValue) {
        setSpacing(8);
        setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(statName.getName().toUpperCase());
        nameLabel.setPrefWidth(112);
        nameLabel.setStyle("""
                    -fx-text-fill: #b9d2df;
                    -fx-font-weight: bold;
                    -fx-font-size: 12px;
                    -fx-letter-spacing: 1px;
                """);

        valueLabel = new Label(String.valueOf(initialValue));
        valueLabel.setPrefWidth(28);
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setStyle("""
                    -fx-font-size: 15px;
                    -fx-font-weight: 900;
                    -fx-text-fill: #f0f2f7;
                    -fx-font-family: 'monospace';
                    -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.24), 8, 0.24, 0, 0);
                """);

        increaseButton = EntityEditorButtonFactory.statIncreaseControl();
        decreaseButton = EntityEditorButtonFactory.statDecreaseControl();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(nameLabel, spacer, valueLabel, increaseButton, decreaseButton);
    }

    /**
     * Updates displayed stat value.
     *
     * @param value new stat value
     */
    public void updateValue(int value) {
        valueLabel.setText(String.valueOf(value));
        playSpark();
    }

    /**
     * Adds action for + button.
     */
    public void addIncreaseAction(Runnable action) {
        increaseButton.setOnAction(e -> action.run());
    }

    /**
     * Adds action for - button.
     */
    public void addDecreaseAction(Runnable action) {
        decreaseButton.setOnAction(e -> action.run());
    }

    private void playSpark() {
        DropShadow glow = new DropShadow(22, Color.web("rgba(175, 196, 216, 0.24)"));
        setEffect(glow);

        ScaleTransition pulseUp = new ScaleTransition(Duration.millis(90), valueLabel);
        pulseUp.setToX(1.24);
        pulseUp.setToY(1.24);

        ScaleTransition pulseDown = new ScaleTransition(Duration.millis(130), valueLabel);
        pulseDown.setToX(1.0);
        pulseDown.setToY(1.0);

        FadeTransition fade = new FadeTransition(Duration.millis(220), this);
        fade.setFromValue(0.82);
        fade.setToValue(1.0);
        fade.setOnFinished(e -> setEffect(null));

        new SequentialTransition(pulseUp, pulseDown, fade).play();
    }
}











