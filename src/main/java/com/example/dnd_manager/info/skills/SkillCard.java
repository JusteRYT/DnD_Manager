package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.nio.file.Path;


/**
 * Visual card representation of a skill.
 * Layout is centered with fixed-size icon, name, meta info, description and remove button.
 */
@Getter
public class SkillCard extends VBox {
    private final Skill skill;

    public SkillCard(Skill skill, Runnable onRemove, Character character) {
        this.skill = skill;
        setSpacing(8);
        setAlignment(Pos.TOP_CENTER);
        setPrefWidth(190);

        String baseStyle = """
                    -fx-background-color: linear-gradient(to bottom, #2b2b2b, #1e1e1e);
                    -fx-background-radius: 10;
                    -fx-border-color: #3a3a3a;
                    -fx-border-radius: 10;
                    -fx-border-width: 1;
                    -fx-padding: 12;
                """;

        String hoverStyle = baseStyle + """
                    -fx-border-color: #c89b3c;
                    -fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.2), 15, 0, 0, 0);
                """;

        setStyle(baseStyle);
        setOnMouseEntered(e -> setStyle(hoverStyle));
        setOnMouseExited(e -> setStyle(baseStyle));

        // Иконка с эффектом свечения
        ImageView iconView = new ImageView();
        iconView.setFitWidth(55);
        iconView.setFitHeight(55);
        iconView.setPreserveRatio(true);
        if (character != null) {
            iconView.setImage(chooseIcon(skill, character));
        } else {
            iconView.setImage(new Image(getClass().getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm()));
        }

        iconView.setStyle("-fx-effect: dropshadow(two-pass-box, black, 10, 0, 0, 0);");

        Label name = new Label(skill.name().toUpperCase());
        name.setStyle("-fx-font-weight: bold; -fx-text-fill: #c89b3c; -fx-font-size: 13px;");
        name.setWrapText(true);

        Label meta = new Label(skill.activationType() + " | " + skill.effectsSummary());
        meta.setStyle("-fx-font-size: 10px; -fx-text-fill: #888;");
        meta.setWrapText(true);

        Label desc = new Label(skill.description());
        desc.setStyle("-fx-font-size: 11px; -fx-text-fill: #ddd; -fx-font-style: italic;");
        desc.setWrapText(true);
        desc.setMaxHeight(60);

        Button removeBtn = AppButtonFactory.deleteButton(30);
        removeBtn.setOnAction(e -> onRemove.run());
        removeBtn.setFocusTraversable(false);
        getChildren().addAll(iconView, name, meta, desc, new Region() {{
            VBox.setVgrow(this, Priority.ALWAYS);
        }}, removeBtn);
    }


    private Image chooseIcon(Skill skill, Character character) {
        return getImage(character, skill.iconPath());
    }

    public static Image getImage(Character character, String iconPath) {
        if (iconPath == null || iconPath.isEmpty()) {
            return new Image(SkillCard.class.getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm());
        }

        try {
            if (iconPath.startsWith("file:") || iconPath.startsWith("jar:") || iconPath.contains("://")) {
                return new Image(iconPath);
            }

            Path path = Path.of(iconPath);
            if (path.isAbsolute()) {
                return new Image(path.toUri().toString());
            }

            if (character != null) {
                return new Image(CharacterAssetResolver.resolve(character.getName(), iconPath));
            }

        } catch (Exception e) {
            System.err.println("Error loading image: " + iconPath + " -> " + e.getMessage());
        }

        return new Image(SkillCard.class.getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm());
    }
}