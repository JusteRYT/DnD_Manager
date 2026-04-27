package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.stats.StatEnum;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class FamiliarStatsSectionBuilder {

    private final FamiliarStatLabelFormatter statLabelFormatter;
    private final FamiliarSectionStyleProvider styleProvider;

    public FamiliarStatsSectionBuilder() {
        this(new FamiliarStatLabelFormatter(), new FamiliarSectionStyleProvider());
    }

    FamiliarStatsSectionBuilder(FamiliarStatLabelFormatter statLabelFormatter, FamiliarSectionStyleProvider styleProvider) {
        this.statLabelFormatter = Objects.requireNonNull(statLabelFormatter, "statLabelFormatter must not be null");
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
    }

    public Node build(Character familiar) {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setAlignment(Pos.CENTER);
        StatEnum[] stats = StatEnum.values();
        for (int i = 0; i < stats.length; i++) {
            grid.add(createStatBlock(stats[i], familiar.getStats().get(stats[i])), i % 6, i / 6);
        }
        return grid;
    }

    private VBox createStatBlock(StatEnum stat, int val) {
        VBox box = new VBox(0);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(60);
        box.setStyle(styleProvider.statBlockStyle());
        Label nameLabel = new Label(statLabelFormatter.shortLabel(stat));
        nameLabel.setStyle(styleProvider.statNameStyle());
        Label valueLabel = new Label(String.valueOf(val));
        valueLabel.setStyle(styleProvider.statValueStyle());
        box.getChildren().addAll(nameLabel, valueLabel);
        return box;
    }
}
