package com.example.dnd_manager.info.stats;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * UI component representing a single character stat row with + and - buttons.
 */
public class StatRow extends HBox {

    private final Label valueLabel;
    private final Button increaseButton;
    private final Button decreaseButton;

    public StatRow(StatEnum statName, int initialValue) {
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(statName.getName());
        nameLabel.setPrefWidth(100);

        valueLabel = new Label(String.valueOf(initialValue));
        valueLabel.setPrefWidth(40);

        increaseButton = new Button("+");
        decreaseButton = new Button("-");

        getChildren().addAll(nameLabel, valueLabel, increaseButton, decreaseButton);
    }

    /**
     * Updates displayed stat value.
     *
     * @param value new stat value
     */
    public void updateValue(int value) {
        valueLabel.setText(String.valueOf(value));
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
}