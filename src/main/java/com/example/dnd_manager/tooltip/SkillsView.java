package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.skills.SkillCardView;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

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
        List<Skill> allSkills = new ArrayList<>(character.getSkills());
        for (InventoryItem item : character.getInventory()) {
            if (item.getAttachedSkills() != null) {
                allSkills.addAll(item.getAttachedSkills());
            }
        }
        for (Skill skill : allSkills) {
            getChildren().add(new SkillCardView(skill, character));
        }
    }
}