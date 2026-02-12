package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lombok.Getter;


/**
 * Visual card representation of a skill.
 * Layout is centered with fixed-size icon, name, meta info, description and remove button.
 */
@Getter
public class SkillCard extends VBox {

    private final Skill skill;

    /**
     * @param skill     skill model
     * @param onRemove  callback invoked when remove button is pressed
     * @param character character owning the skill (for resolving assets)
     */
    public SkillCard(Skill skill, Runnable onRemove, Character character) {
        this.skill = skill;

        setSpacing(6);
        setAlignment(Pos.TOP_CENTER);
        setStyle("""
                -fx-border-color: gray;
                -fx-padding: 10;
                -fx-background-color: %s;
                -fx-border-radius: 6;
                -fx-background-radius: 6;
                """.formatted(AppTheme.BACKGROUND_PRIMARY));
        setPrefWidth(180);

        // Icon
        ImageView icon = new ImageView();
        icon.setFitWidth(60);
        icon.setFitHeight(60);
        icon.setPreserveRatio(true);
        icon.setSmooth(true);
        Image iconImage = chooseIcon(skill, character);
        if (iconImage != null) {
            icon.setImage(iconImage);
        }

        // Name
        Label name = new Label(skill.name());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        name.setWrapText(true);
        name.setAlignment(Pos.CENTER);

        // Meta info
        Label meta = new Label(skill.activationType() + I18n.t("label.skillCards.effect") + skill.effectsSummary());
        meta.setStyle("-fx-font-size: 11; -fx-text-fill: #888888;");
        meta.setWrapText(true);
        meta.setAlignment(Pos.CENTER);

        // Description
        Label description = new Label(skill.description());
        description.setWrapText(true);
        description.setAlignment(Pos.CENTER);
        description.setMaxWidth(160);
        description.setStyle("-fx-font-size: 12;");

        // Remove button
        Button removeButton = AppButtonFactory.customButton("×", 40, AppTheme.BUTTON_REMOVE, AppTheme.BUTTON_REMOVE_HOVER);
        removeButton.setOnAction(e -> onRemove.run());
        removeButton.setFocusTraversable(false);
        getChildren().addAll(icon, name, meta, description, removeButton);
    }


    private Image chooseIcon(Skill skill, Character character) {
        Image image = null;
        if (skill.iconPath() != null && !skill.iconPath().isEmpty() && character != null) {
            image = new Image(CharacterAssetResolver.resolve(character.getName(), skill.iconPath()));
        } else {
            if (skill.iconPath() != null && !skill.iconPath().isEmpty()) {
                image = new Image("file:" + skill.iconPath());
            }
        }

        return image;
    }

}