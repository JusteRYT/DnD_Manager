package com.example.dnd_manager.info.stats;


import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.screen.FormMode;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

/**
 * Editor component for character statistics.
 * Supports CREATE and EDIT modes without напрямую изменять Stats.
 */
public class StatsEditor extends VBox {

    @Getter
    private final Map<StatEnum, Integer> values = new EnumMap<>(StatEnum.class);
    private final Map<StatEnum, StatRow> rows = new EnumMap<>(StatEnum.class);


    public StatsEditor(Stats stats, FormMode mode) {
        setSpacing(10);

        // Инициализация значений: 0 для CREATE, существующие для EDIT
        for (StatEnum stat : StatEnum.values()) {
            int initialValue = (mode == FormMode.EDIT) ? stats.get(stat) : 0;
            values.put(stat, initialValue);

            StatRow row = new StatRow(stat, initialValue);

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


            rows.put(stat, row);
            getChildren().add(row);
        }
    }

    /**
     * Apply current editor values to the domain Stats object.
     */
    public void applyTo(Character character) {
        for (StatEnum stat : StatEnum.values()) {
            character.getStats().set(stat.name(), values.get(stat));
        }
    }
}
