package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.info.skills.Skill;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.util.List;

/**
 * View for displaying character skills as icons with tooltips.
 */
public class SkillsView extends FlowPane {

    public SkillsView(List<Skill> skills) {
        super(10, 10);
        setPrefWrapLength(400);

        for (Skill skill : skills) {
            ImageView icon = new ImageView();
            icon.setFitWidth(48);
            icon.setFitHeight(48);

            if (skill.iconPath() != null) {
                icon.setImage(new Image("file:" + skill.iconPath()));
            }

            Tooltip tooltip = TooltipFactory.createSkillTooltip(skill);
            Tooltip.install(icon, tooltip);

            icon.setStyle("-fx-cursor: hand;");

            getChildren().add(icon);
        }
    }
}