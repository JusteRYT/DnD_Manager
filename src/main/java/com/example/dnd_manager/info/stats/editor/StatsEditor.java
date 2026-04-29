package com.example.dnd_manager.info.stats.editor;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.stats.model.StatEnum;
import com.example.dnd_manager.info.stats.model.Stats;
import com.example.dnd_manager.info.stats.view.StatRow;
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
    private final StatsEditorStyleProvider styleProvider = new StatsEditorStyleProvider();

    public StatsEditor(Stats stats, FormMode mode) {
        setSpacing(8);
        setAlignment(Pos.TOP_CENTER);

        for (StatEnum stat : StatEnum.values()) {
            int initialValue = (mode == FormMode.EDIT) ? stats.get(stat) : 0;
            values.put(stat, initialValue);

            StatRow row = new StatRow(stat, initialValue);

            row.setStyle(styleProvider.rowStyle(false));
            row.setOnMouseEntered(e -> row.setStyle(styleProvider.rowStyle(true)));
            row.setOnMouseExited(e -> row.setStyle(styleProvider.rowStyle(false)));

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
            values.put(stat, values.get(stat) - 1);
            row.updateValue(values.get(stat));
            stats.decrease(stat);

        });
    }

    public void applyTo(Character character) {
        for (StatEnum stat : StatEnum.values()) {
            character.getStats().set(stat.name(), values.get(stat));
        }
    }
}











