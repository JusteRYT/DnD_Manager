package com.example.dnd_manager.info.stats;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Map;

/**
 * RPG-style stats grid with highlighted values.
 */
public class StatsGridView extends GridPane {

    public StatsGridView(Map<StatEnum, Integer> stats) {
        setHgap(12);
        setVgap(8);
        setPadding(new Insets(10));

        int row = 0;
        for (var entry : stats.entrySet()) {
            Label name = new Label(entry.getKey().getName());
            name.setStyle("-fx-text-fill: #b0b0b0;");

            Label value = new Label(String.valueOf(entry.getValue()));
            value.setStyle("""
                    -fx-text-fill: #f2f2f2;
                    -fx-font-weight: bold;
                    """);

            StackPane valueBox = new StackPane(value);
            valueBox.setStyle("""
                    -fx-background-color: #1e1e1e;
                    -fx-background-radius: 6;
                    -fx-padding: 4 10;
                    """);

            add(name, 0, row);
            add(valueBox, 1, row);
            row++;
        }
    }
}
