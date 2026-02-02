package com.example.dnd_manager.screen;

import com.example.dnd_manager.info.stats.StatsEditor;
import com.example.dnd_manager.tooltip.BuffsView;
import com.example.dnd_manager.tooltip.SkillsView;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import com.example.dnd_manager.domain.Character;

/**
 * Screen for viewing a character.
 */
public class CharacterViewScreen {

    private final Character character;

    public CharacterViewScreen(Character character) {
        this.character = character;
    }

    public Parent getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        root.setTop(buildHeader());
        root.setCenter(buildMainContent());
        root.setBottom(buildDescription());

        return root;
    }

    private Parent buildHeader() {
        Label header = new Label(
                character.getName() + " | " +
                        character.getRace() + " | " +
                        character.getCharacterClass()
        );
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        return header;
    }

    private Parent buildMainContent() {
        GridPane grid = new GridPane();
        grid.setHgap(20);

        VBox left = new VBox(10,
                new Label("Stats"),
                new StatsEditor(character.getStats()),
                new Label("Buffs / Debuffs"),
                new BuffsView(character.getBuffs())
        );

        VBox right = new VBox(10,
                new Label("Skills"),
                new SkillsView(character.getSkills())
        );

        grid.add(left, 0, 0);
        grid.add(right, 1, 0);

        return grid;
    }

    private Parent buildDescription() {
        Label text = new Label(character.getDescription());
        text.setWrapText(true);
        return text;
    }
}
