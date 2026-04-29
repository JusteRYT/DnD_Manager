package com.example.dnd_manager.info.stats.view;

import com.example.dnd_manager.info.stats.model.StatEnum;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Map;

public class StatsGridView extends GridPane {

    public StatsGridView(Map<StatEnum, Integer> stats) {
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(10));

        javafx.scene.layout.ColumnConstraints col1 = new javafx.scene.layout.ColumnConstraints();
        col1.setPercentWidth(50);
        javafx.scene.layout.ColumnConstraints col2 = new javafx.scene.layout.ColumnConstraints();
        col2.setPercentWidth(50);
        getColumnConstraints().addAll(col1, col2);

        int index = 0;
        for (var entry : stats.entrySet()) {
            VBox statCard = createStatCard(entry.getKey().getName(), entry.getValue());

            int col = index % 2;
            int row = index / 2;

            add(statCard, col, row);
            index++;
        }
    }

    private VBox createStatCard(String name, Integer value) {
        Label nameLabel = new Label(name.toUpperCase());
        nameLabel.setStyle("-fx-text-fill: #aab8cf; -fx-font-size: 11px; -fx-font-weight: bold;");

        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.setStyle("-fx-text-fill: #e9edf3; -fx-font-size: 24px; -fx-font-weight: bold;");

        VBox card = getVBox(nameLabel, valueLabel);

        GridPane.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private static VBox getVBox(Label nameLabel, Label valueLabel) {
        VBox card = new VBox(2, nameLabel, valueLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));

        card.setStyle("""
                -fx-background-color: #11172a;
                -fx-background-radius: 8;
                -fx-border-color: #293550;
                -fx-border-radius: 8;
                -fx-border-width: 1;
                """);

        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-border-color: #b7c9dd;"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace("-fx-border-color: #b7c9dd;", "-fx-border-color: #293550;")));
        return card;
    }
}











