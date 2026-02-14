package com.example.dnd_manager.overview.utils;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.stats.StatsGridView;
import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Left panel: Stats with Steel/Silver glow
 */
public class StatsPanel extends VBox {

    public StatsPanel(Character character) {
        setSpacing(10);

        Label title = new Label(I18n.t("stats.label.overview").toUpperCase());
        title.setStyle("""
            -fx-text-fill: #b0b0b0; 
            -fx-font-size: 14px; 
            -fx-font-weight: bold;
            -fx-letter-spacing: 2px;
            """);

        VBox statsBox = new VBox(10, title, new StatsGridView(character.getStats().asMap()));
        statsBox.setPadding(new Insets(20));

        // --- СТАЛЬНОЙ СТИЛЬ ---
        statsBox.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1e1e1e);
            -fx-background-radius: 12;
            -fx-border-color: #4f4f4f; /* Цвет пушечной стали */
            -fx-border-radius: 12;
            -fx-border-width: 1;
            /* Холодное белое/стальное свечение */
            -fx-effect: dropshadow(three-pass-box, rgba(255, 255, 255, 0.08), 15, 0, 0, 0);
            """);

        getChildren().add(statsBox);
    }
}