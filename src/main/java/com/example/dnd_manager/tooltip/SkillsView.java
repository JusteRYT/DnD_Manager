package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.skills.SkillCardView;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * Grid-based view for displaying skill cards.
 * Ensures consistent alignment and predictable layout.
 */
public class SkillsView extends GridPane {

    private static final int COLUMNS = 5;

    public SkillsView(Character character) {
        setHgap(20);
        setVgap(20);
        setPadding(new Insets(20));

        for (int i = 0; i < character.getSkills().size(); i++) {
            int column = i % COLUMNS;
            int row = i / COLUMNS;

            SkillCardView card = new SkillCardView(character.getSkills().get(i), character.getName());
            card.setPrefWidth(Region.USE_COMPUTED_SIZE);
            card.setMinWidth(210);

            add(card, column, row);
        }
    }
}