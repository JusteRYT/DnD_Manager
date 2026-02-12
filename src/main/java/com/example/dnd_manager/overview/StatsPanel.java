package com.example.dnd_manager.overview;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.stats.StatsGridView;
import com.example.dnd_manager.lang.I18n;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Left panel: Stats
 */
public class StatsPanel extends VBox {

    public StatsPanel(Character character) {
        setSpacing(10);
        Label title = new Label(I18n.t("stats.label"));
        title.setStyle("-fx-text-fill: #c89b3c; -fx-font-size: 16px; -fx-font-weight: bold;");

        VBox statsBox = new VBox(title, new StatsGridView(character.getStats().asMap()));
        statsBox.setStyle("-fx-background-color: #252526; -fx-background-radius: 8; -fx-padding: 10;");

        getChildren().add(statsBox);
    }
}
