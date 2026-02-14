package com.example.dnd_manager.info.stats;

import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

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

        // 1. Название стата: используем капс и золотистый цвет из твоей темы
        Label nameLabel = new Label(statName.getName().toUpperCase());
        nameLabel.setPrefWidth(120); // Немного уменьшим, если названия короткие
        nameLabel.setStyle("""
                    -fx-text-fill: #c89b3c; 
                    -fx-font-weight: bold; 
                    -fx-font-size: 12px;
                    -fx-letter-spacing: 1px;
                """);

        // 2. Значение: сделаем его ярче и белее
        valueLabel = new Label(String.valueOf(initialValue));
        valueLabel.setPrefWidth(30);
        valueLabel.setAlignment(Pos.CENTER); // Центрируем число
        valueLabel.setStyle("""
                    -fx-font-size: 15px; 
                    -fx-font-weight: 900; 
                    -fx-text-fill: #FFFFFF;
                    -fx-font-family: 'monospace'; /* Моноширинный, чтобы число не прыгало */
                """);

        increaseButton = AppButtonFactory.createValueAdjustButton(true, 25, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);
        decreaseButton = AppButtonFactory.createValueAdjustButton(false, 25, AppTheme.BUTTON_REMOVE, AppTheme.BUTTON_REMOVE_HOVER);

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