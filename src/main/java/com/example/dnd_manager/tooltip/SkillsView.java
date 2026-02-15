package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.skills.SkillCardView;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

/**
 * Теперь использует FlowPane для автоматического заполнения строки
 */
public class SkillsView extends FlowPane {

    public SkillsView(Character character) {
        setHgap(20);
        setVgap(20);
        setPadding(new Insets(20));
        setPrefWidth(Region.USE_COMPUTED_SIZE);

        for (int i = 0; i < character.getSkills().size(); i++) {
            SkillCardView card = new SkillCardView(character.getSkills().get(i), character.getName());
            getChildren().add(card);
        }
    }
}