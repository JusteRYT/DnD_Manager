package com.example.dnd_manager.screen;

import com.example.dnd_manager.info.avatar.AvatarPicker;
import com.example.dnd_manager.info.buff_debuff.BuffEditor;
import com.example.dnd_manager.info.inventory.InventoryEditor;
import com.example.dnd_manager.info.skills.SkillsEditor;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.text.BaseInfoForm;
import com.example.dnd_manager.info.text.CharacterDescriptionSection;
import com.example.dnd_manager.info.stats.Stats;
import com.example.dnd_manager.info.stats.StatsEditor;
import com.example.dnd_manager.store.StorageService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



/**
 * Screen for creating a new D&D character.
 */
public class CharacterCreateScreen {

    private final Stage stage;
    private final Stats stats = new Stats();
    private CharacterDescriptionSection descriptionSection;
    private BuffEditor buffEditor;
    private final StorageService storageService;

    public CharacterCreateScreen(Stage stage, StorageService storageService) {
        this.stage = stage;
        this.storageService = storageService;
    }

    /**
     * Builds and returns character creation view.
     *
     * @return root view node
     */
    public Parent getView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        root.setTop(buildTitle());
        root.setCenter(buildForm());

        return root;
    }

    private Label buildTitle() {
        Label title = new Label("Character Creation");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(title, Pos.CENTER);
        return title;
    }

    private VBox buildForm() {
        VBox form = new VBox(20);
        descriptionSection = new CharacterDescriptionSection();
        buffEditor = new BuffEditor();
        form.getChildren().add(buildBaseInfoSection());
        form.getChildren().add(buildTextSections());
        form.getChildren().add(buildStatsSection());
        form.getChildren().add(descriptionSection);
        form.getChildren().add(buffEditor);
        form.getChildren().addAll(
                new InventoryEditor(),
                new SkillsEditor()
        );
        Button saveButton = new Button("Save & View Character");
        saveButton.setOnAction(event -> saveAndShowOverview());
        return form;
    }

    private void saveAndShowOverview() {
        Character character = new Character();

        // Здесь нужно собрать данные из всех секций
        // Пример:
        character.setName("Default Name"); // заменить на данные из BaseInfoForm
        character.setRace("Human");         // заменить на данные из BaseInfoForm
        character.setCharacterClass("Ranger"); // тоже
        character.setDescription(descriptionSection.getDescription());
        character.setPersonality(descriptionSection.getPersonality());
        character.setBackstory(descriptionSection.getBackstory());
        character.getStats().copyForm(stats);
        character.getBuffs().addAll(buffEditor.getBuffs());
        // inventory и skills можно аналогично

        // Сохраняем через StorageService
        storageService.saveCharacter(character);

        // Переходим на экран обзора
        CharacterOverviewScreen overviewScreen = new CharacterOverviewScreen(character);
        stage.getScene().setRoot(overviewScreen);
    }

    private GridPane buildBaseInfoSection() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);

        AvatarPicker avatarPicker = new AvatarPicker();
        BaseInfoForm baseInfoForm = new BaseInfoForm();

        grid.add(avatarPicker, 0, 0);
        grid.add(baseInfoForm, 1, 0);

        return grid;
    }

    private VBox buildTextSections() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Description"));
        box.getChildren().add(new Label("Personality"));
        box.getChildren().add(new Label("Backstory"));
        return box;
    }

    private VBox buildStatsSection() {
        VBox box = new VBox(10);
        box.getChildren().add(new Label("Attributes"));
        box.getChildren().add(new StatsEditor(stats));
        return box;
    }
}
