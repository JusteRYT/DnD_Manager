package com.example.dnd_manager.info.skills;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Visual card representation of a skill.
 */
public class SkillCard extends VBox {

    public SkillCard(Skill skill) {
        setSpacing(5);
        setStyle("-fx-border-color: gray; -fx-padding: 8;");

        ImageView icon = new ImageView();
        icon.setFitWidth(40);
        icon.setFitHeight(40);

        if (skill.iconPath() != null) {
            icon.setImage(new Image("file:" + skill.iconPath()));
        }

        Label name = new Label(skill.name());
        name.setStyle("-fx-font-weight: bold;");

        Label meta = new Label(
                skill.activationType() + " | Damage: " + skill.damage()
        );

        Label description = new Label(skill.description());
        description.setWrapText(true);

        getChildren().addAll(icon, name, meta, description);
    }
}
