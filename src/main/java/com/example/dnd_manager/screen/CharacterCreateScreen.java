package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.avatar.AvatarPicker;
import com.example.dnd_manager.info.buff_debuff.BuffEditor;
import com.example.dnd_manager.info.inventory.InventoryEditor;
import com.example.dnd_manager.info.skills.SkillsEditor;
import com.example.dnd_manager.info.stats.Stats;
import com.example.dnd_manager.info.stats.StatsEditor;
import com.example.dnd_manager.info.text.BaseInfoForm;
import com.example.dnd_manager.info.text.CharacterDescriptionSection;
import com.example.dnd_manager.info.text.dto.AvatarData;
import com.example.dnd_manager.info.text.dto.BaseInfoData;
import com.example.dnd_manager.info.text.dto.CharacterDescriptionData;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.SectionBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;


/**
 * Screen for creating a new D&D character.
 */
public class CharacterCreateScreen extends AbstractScreen {

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


    protected Label buildTitle() {
        Label title = new Label(I18n.t("label.title.create_character"));
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: orange");
        BorderPane.setAlignment(title, Pos.CENTER);
        return title;
    }

    protected VBox buildForm() {
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

        Button saveButton = AppButtonFactory.primary(I18n.t("button.saveAndView"));
        saveButton.setOnAction(event -> saveAndShowOverview());

        Button exitButton = AppButtonFactory.customButton(I18n.t("button.exit"), 100);
        exitButton.setOnAction(event -> exitScreen());

        HBox buttonBox = new HBox(10);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBox.getChildren().addAll(saveButton, spacer, exitButton);
        form.getChildren().add(buttonBox);
        return form;
    }

    private void exitScreen() {
        StartScreen startScreen = new StartScreen(stage, storageService);
        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(startScreen.getView());
        scrollPane.setFitToHeight(true);
        stage.getScene().setRoot(scrollPane);
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
        BaseInfoData baseInfo = baseInfoForm.getData();
        AvatarData avatarData = avatarPicker.getData();
        CharacterDescriptionData descriptionData = descriptionSection.getData();
        character.setName(baseInfo.name());
        character.setRace(baseInfo.race());
        character.setCharacterClass(baseInfo.characterClass());
        character.setHp(baseInfo.hp());
        character.setArmor(baseInfo.armor());
        character.setAvatarImage(avatarData.imagePath());
        character.setCurrentMana(baseInfo.mana());
        character.setMaxMana(baseInfo.mana());
        character.setLevel(baseInfo.level());

        // Text info
        character.setDescription(descriptionData.description());
        character.setPersonality(descriptionData.personality());
        character.setBackstory(descriptionData.backstory());
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

        Label titleStats = new Label(I18n.t("stats.label"));
        titleStats.setStyle("""
                    -fx-font-size: 16px;
                    -fx-font-weight: bold;
                    -fx-text-fill: %s;
                """.formatted(AppTheme.TEXT_ACCENT));

        StatsEditor statsEditor = new StatsEditor(stats, FormMode.CREATE);

        sectionBox.getChildren().addAll(titleStats, statsEditor);

        return sectionBox;
    }
}
