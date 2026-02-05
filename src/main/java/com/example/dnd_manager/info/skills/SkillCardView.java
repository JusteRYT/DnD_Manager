package com.example.dnd_manager.info.skills;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * UI card component for displaying detailed information about a skill.
 * Follows Card View UI pattern.
 */
public class SkillCardView extends VBox {

    public SkillCardView(Skill skill) {
        setSpacing(10);
        setPadding(new Insets(14));
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(200, 260);

        setStyle("""
                -fx-background-color: #1e1e1e;
                -fx-background-radius: 14;
                -fx-border-radius: 14;
                -fx-border-color: #3a3a3a;
                """);

        ImageView icon = createIcon(skill);
        Label name = createName(skill);
        Label activation = createActivation(skill);
        Label damage = createDamage(skill);
        Label description = createDescription(skill);

        getChildren().addAll(icon, name, activation, damage, description);
    }

    private ImageView createIcon(Skill skill) {
        ImageView icon = new ImageView(new Image("file:" + skill.iconPath()));
        icon.setFitWidth(80);
        icon.setFitHeight(80);
        return icon;
    }

    private Label createName(Skill skill) {
        Label label = new Label(skill.name());
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #c89b3c;");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private Label createActivation(Skill skill) {
        Label label = new Label("Activation: " + skill.activationType());
        label.setStyle("-fx-text-fill: #9cdcfe; -fx-font-size: 13px;");
        return label;
    }

    private Label createDamage(Skill skill) {
        Label label = new Label("Damage: " + skill.damage());
        label.setStyle("-fx-text-fill: #f44747; -fx-font-size: 13px;");
        return label;
    }

    private Label createDescription(Skill skill) {
        Label label = new Label(skill.description());
        label.setWrapText(true);
        label.setMaxHeight(40);
        label.setStyle("-fx-text-fill: #d4d4d4; -fx-font-size: 12px;");
        return label;
    }
}
