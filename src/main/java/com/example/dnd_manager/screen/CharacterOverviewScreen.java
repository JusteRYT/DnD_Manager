package com.example.dnd_manager.screen;

import com.example.dnd_manager.tooltip.BuffsView;
import com.example.dnd_manager.tooltip.SkillsView;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import com.example.dnd_manager.domain.Character;

/**
 * Main character overview screen.
 * Displays all important character data on one screen.
 */
public class CharacterOverviewScreen extends BorderPane {

    public CharacterOverviewScreen(Character character) {
        setPadding(new Insets(15));

        setTop(createHeader(character));
        setCenter(createMainContent(character));
    }

    private HBox createHeader(Character character) {
        ImageView avatar = new ImageView(new Image("file:assets/avatar_placeholder.png"));
        avatar.setFitWidth(96);
        avatar.setFitHeight(96);

        VBox info = new VBox(5,
                new Label(character.getName()),
                new Label(character.getRace() + " • " + character.getCharacterClass())
        );

        info.setStyle("-fx-font-size: 16px;");

        return new HBox(15, avatar, info);
    }

    private GridPane createMainContent(Character character) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // Left column
        VBox description = createTextBlock("Description", character.getDescription());
        VBox stats = createTextBlock("Stats", character.getStats().toString());

        // Right column (MAIN)
        VBox buffs = new VBox(
                new Label("Buffs / Debuffs"),
                new BuffsView(character.getBuffs())
        );

        VBox skills = new VBox(
                new Label("Skills"),
                new SkillsView(character.getSkills())
        );

        grid.add(description, 0, 0);
        grid.add(stats, 0, 1);

        grid.add(buffs, 1, 0);
        grid.add(skills, 1, 1);

        ColumnConstraints left = new ColumnConstraints();
        left.setPercentWidth(40);

        ColumnConstraints right = new ColumnConstraints();
        right.setPercentWidth(60);

        grid.getColumnConstraints().addAll(left, right);

        return grid;
    }

    private VBox createTextBlock(String title, String content) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label text = new Label(content);
        text.setWrapText(true);

        VBox box = new VBox(5, titleLabel, text);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");

        return box;
    }
}
