package com.example.dnd_manager.info.stats;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Map;

/**
 * Отображение статов в виде красивых карточек-плиток.
 */
public class StatsGridView extends GridPane {

    public StatsGridView(Map<StatEnum, Integer> stats) {
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10));

        // Настраиваем колонки, чтобы они делили место поровну
        javafx.scene.layout.ColumnConstraints col1 = new javafx.scene.layout.ColumnConstraints();
        col1.setPercentWidth(50);
        javafx.scene.layout.ColumnConstraints col2 = new javafx.scene.layout.ColumnConstraints();
        col2.setPercentWidth(50);
        getColumnConstraints().addAll(col1, col2);

        int index = 0;
        for (var entry : stats.entrySet()) {
            // Создаем карточку для одного стата
            VBox statCard = createStatCard(entry.getKey().getName(), entry.getValue());

            // Расставляем: 2 колонки
            int col = index % 2;
            int row = index / 2;

            add(statCard, col, row);
            index++;
        }
    }

    private VBox createStatCard(String name, Integer value) {
        Label nameLabel = new Label(name.toUpperCase());
        nameLabel.setStyle("-fx-text-fill: #8a8a8a; -fx-font-size: 11px; -fx-font-weight: bold;");

        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.setStyle("-fx-text-fill: #e0e0e0; -fx-font-size: 24px; -fx-font-weight: bold;");

        // Вычисляем модификатор (опционально, если хочешь добавить логику D&D)
        // int mod = (value - 10) / 2;
        // Label modLabel = new Label((mod >= 0 ? "+" : "") + mod);
        // modLabel.setStyle("-fx-text-fill: #c89b3c; -fx-font-size: 14px;");

        VBox card = getVBox(nameLabel, valueLabel);

        GridPane.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private static VBox getVBox(Label nameLabel, Label valueLabel) {
        VBox card = new VBox(2, nameLabel, valueLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));

        // Стиль карточки: темный фон, скругление, легкая обводка
        card.setStyle("""
                -fx-background-color: #2b2b2b;
                -fx-background-radius: 8;
                -fx-border-color: #3e3e3e;
                -fx-border-radius: 8;
                -fx-border-width: 1;
                """);

        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-border-color: #c89b3c;"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace("-fx-border-color: #c89b3c;", "-fx-border-color: #3e3e3e;")));
        return card;
    }
}