package com.example.dnd_manager.info.stats;

import javafx.scene.layout.VBox;

/**
 * Editor component for character statistics.
 */
public class StatsEditor extends VBox {



    public StatsEditor(Stats stats) {
        setSpacing(10);

        for (StatEnum stat : StatEnum.values()) {
            StatRow row = createStatRow(stat, stats);
            getChildren().add(row);
        }

    }

    /**
     * Creates a single row for a stat with + and - buttons.
     *
     * @param stat  stat type
     * @param stats Stats object
     * @return StatRow with buttons and value label
     */
    private StatRow createStatRow(StatEnum stat, Stats stats) {
        StatRow row = new StatRow(stat, stats.get(stat));

        row.addIncreaseAction(() -> {
            stats.increase(stat);
            row.updateValue(stats.get(stat));
        });

        row.addDecreaseAction(() -> {
            stats.decrease(stat);
            row.updateValue(stats.get(stat));
        });

        return row;
    }
}
