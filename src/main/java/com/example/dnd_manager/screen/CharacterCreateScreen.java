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
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.SectionBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
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

    private BaseInfoForm baseInfoForm;
    private InventoryEditor inventoryEditor;
    private SkillsEditor skillsEditor;
    private AvatarPicker avatarPicker;

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

        root.setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + ";");

        root.setTop(buildTitle());

        VBox form = buildForm();

        ScrollPane scrollPane = new ScrollPane(form);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        root.setCenter(scrollPane);

        return root;
    }

    private Label buildTitle() {
        Label title = new Label("Character Creation");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: orange");
        BorderPane.setAlignment(title, Pos.CENTER);
        return title;
    }

    private VBox buildForm() {
        VBox form = new VBox(20);

        descriptionSection = new CharacterDescriptionSection();
        buffEditor = new BuffEditor();
        inventoryEditor = new InventoryEditor();
        skillsEditor = new SkillsEditor();

        form.getChildren().addAll(
                new SectionBox(buildBaseInfoSection()),
                new SectionBox(descriptionSection),
                new SectionBox(buffEditor),
                new SectionBox(inventoryEditor),
                new SectionBox(skillsEditor)
        );

        Button saveButton = AppButtonFactory.primary("Save & View Character");
        saveButton.setOnAction(event -> saveAndShowOverview());

        form.getChildren().add(saveButton);
        return form;
    }

    private void saveAndShowOverview() {
        Character character = getCharacter();

        // Stats
        character.getStats().copyFrom(stats);

        // Buffs
        character.getBuffs().addAll(buffEditor.getBuffs());

        // Inventory
        character.getInventory().addAll(inventoryEditor.getItems());

        // Skills
        character.getSkills().addAll(skillsEditor.getSkills());

        // Сохраняем в JSON
        storageService.saveCharacter(character);

        // Переходим на экран обзора
        CharacterOverviewScreen overviewScreen = new CharacterOverviewScreen(character.getName(), storageService);
        stage.getScene().setRoot(overviewScreen);
    }

    private Character getCharacter() {
        Character character = new Character();

        // Base info
        character.setName(baseInfoForm.getName());
        character.setRace(baseInfoForm.getRace());
        character.setCharacterClass(baseInfoForm.getCharacterClass());
        character.setAvatarImage(avatarPicker.getImage());

        // Text info
        character.setDescription(descriptionSection.getDescription());
        character.setPersonality(descriptionSection.getPersonality());
        character.setBackstory(descriptionSection.getBackstory());
        return character;
    }

    private HBox buildBaseInfoSection() {
        HBox row = new HBox(20);
        row.setPadding(new Insets(10));

        avatarPicker = new AvatarPicker();
        baseInfoForm = new BaseInfoForm();
        VBox statsSection = buildStatsSection();

        HBox statsContainer = new HBox();
        statsContainer.getChildren().add(statsSection);
        statsContainer.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(statsContainer, Priority.ALWAYS);

        row.getChildren().addAll(avatarPicker, baseInfoForm, statsContainer);

        return row;
    }

    private VBox buildStatsSection() {
        VBox sectionBox = new VBox(10);
        sectionBox.setPadding(new Insets(12));
        sectionBox.setStyle("""
                    -fx-background-color: %s;
                    -fx-background-radius: 8;
                    -fx-border-radius: 8;
                    -fx-border-color: %s;
                """.formatted(AppTheme.BACKGROUND_SECONDARY, AppTheme.BORDER_MUTED));

        Label titleStats = new Label("Stats");
        titleStats.setStyle("""
                    -fx-font-size: 16px;
                    -fx-font-weight: bold;
                    -fx-text-fill: %s;
                """.formatted(AppTheme.TEXT_ACCENT));

        StatsEditor statsEditor = new StatsEditor(stats);

        sectionBox.getChildren().addAll(titleStats, statsEditor);

        return sectionBox;
    }
}
