package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.skills.SkillCardView;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Grid-based view for displaying skill cards.
 * Ensures consistent alignment and predictable layout.
 */
public class SkillsView extends GridPane {

    private static final int COLUMNS = 5;

    public SkillsView(List<Skill> skills) {
        setHgap(20);
        setVgap(20);
        setPadding(new Insets(20));

        for (int i = 0; i < skills.size(); i++) {
            int column = i % COLUMNS;
            int row = i / COLUMNS;

            SkillCardView card = new SkillCardView(skills.get(i));
            card.setPrefWidth(160);
            card.setMinHeight(220);

            add(card, column, row);
        }
    }
}