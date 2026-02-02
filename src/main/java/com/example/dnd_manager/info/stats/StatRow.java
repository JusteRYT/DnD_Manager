package com.example.dnd_manager.info.stats;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * UI component representing a single character stat row.
 */
public class StatRow extends HBox {

    private final Label valueLabel;

    public StatRow(String statName, int initialValue, Runnable onIncrease) {
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(statName);
        nameLabel.setPrefWidth(100);

        valueLabel = new Label(String.valueOf(initialValue));
        valueLabel.setPrefWidth(40);

        Button increaseButton = new Button("+");
        increaseButton.setOnAction(e -> {
            onIncrease.run();
        });

        getChildren().addAll(nameLabel, valueLabel, increaseButton);
    }

    /**
     * Updates displayed stat value.
     *
     * @param value new stat value
     */
    public void updateValue(int value) {
        valueLabel.setText(String.valueOf(value));
    }
}
