package com.example.dnd_manager.info.stats;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.screen.FormMode;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

/**
 * Stats Editor.
 * Визуально - вертикальный стек. Стилизация контейнера происходит снаружи.
 */
public class StatsEditor extends VBox {

    @Getter
    private final Map<StatEnum, Integer> values = new EnumMap<>(StatEnum.class);
    private final Map<StatEnum, StatRow> rows = new EnumMap<>(StatEnum.class);


    public StatsEditor(Stats stats, FormMode mode) {
        setSpacing(8);
        setAlignment(Pos.TOP_CENTER);

        for (StatEnum stat : StatEnum.values()) {
            int initialValue = (mode == FormMode.EDIT) ? stats.get(stat) : 0;
            values.put(stat, initialValue);

            StatRow row = new StatRow(stat, initialValue);

            // Базовый стиль плашки
            String baseStyle = """
                    -fx-background-color: linear-gradient(to right, #252526, #1e1e1e);
                    -fx-background-radius: 6;
                    -fx-border-radius: 6;
                    -fx-border-color: #3a3a3a;
                    -fx-border-width: 1;
                    -fx-padding: 8 12;
                    """;

            // Стиль при наведении
            String hoverStyle = baseStyle + """
                    -fx-border-color: #c89b3c;
                    -fx-effect: dropshadow(three-pass-box, rgba(255, 193, 7, 0.15), 10, 0, 0, 0);
                    """;

            row.setStyle(baseStyle);
            row.setOnMouseEntered(e -> row.setStyle(hoverStyle));
            row.setOnMouseExited(e -> row.setStyle(baseStyle));

            // Логика кнопок...
            setupActions(row, stat, stats);

            getChildren().add(row);
        }
    }

    private void setupActions(StatRow row, StatEnum stat, Stats stats) {
        row.addIncreaseAction(() -> {
            values.put(stat, values.get(stat) + 1);
            row.updateValue(values.get(stat));
            stats.increase(stat);
        });
        row.addDecreaseAction(() -> {
            if (values.get(stat) > 0) {
                values.put(stat, values.get(stat) - 1);
                row.updateValue(values.get(stat));
                stats.decrease(stat);
            }
        });
    }

    public void applyTo(Character character) {
        for (StatEnum stat : StatEnum.values()) {
            character.getStats().set(stat.name(), values.get(stat));
        }
    }
}