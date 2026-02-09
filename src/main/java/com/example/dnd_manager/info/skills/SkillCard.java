package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * Visual card representation of a skill.
 */
@Getter
public class SkillCard extends VBox {


    private final Skill skill;

    /**
     * @param skill    skill model
     * @param onRemove callback invoked when remove button is pressed
     */
    public SkillCard(Skill skill, Runnable onRemove) {
        this.skill = skill;

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

        Button removeButton = AppButtonFactory.customButton("х", 40, AppTheme.BUTTON_REMOVE, AppTheme.BUTTON_REMOVE_HOVER);
        removeButton.setOnAction(e -> onRemove.run());

        getChildren().addAll(icon, name, meta, description, removeButton);
    }

}