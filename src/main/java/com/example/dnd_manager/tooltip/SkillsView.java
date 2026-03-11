package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
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

        refresh(character);
    }

    public void refresh(Character character) {
        getChildren().clear();
        for (Skill skill : character.getSkills()) {
            getChildren().add(new SkillCardView(skill, character, null));
        }
        for (InventoryItem item : character.getInventory()) {
            if (item.getAttachedSkills() != null) {
                for (Skill skill : item.getAttachedSkills()) {
                    getChildren().add(new SkillCardView(skill, character, item));
                }
            }
        }
    }
}