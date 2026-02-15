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
 * Screen for editing an existing D&D character.
 */
public class CharacterEditScreen extends AbstractScreen {

    private final Stage stage;
    private final Character character;
    private final StorageService storageService;

    private BaseInfoForm baseInfoForm;
    private CharacterDescriptionSection descriptionSection;
    private AvatarPicker avatarPicker;
    private StatsEditor statsEditor;
    private BuffEditor buffEditor;
    private InventoryEditor inventoryEditor;
    private SkillsEditor skillsEditor;

    public CharacterEditScreen(Stage stage, Character character, StorageService storageService) {
        this.stage = stage;
        this.character = character;
        this.storageService = storageService;
    }

    protected Label buildTitle() {
        Label title = new Label(I18n.t("title.editScreen"));
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: orange");
        BorderPane.setAlignment(title, Pos.CENTER);
        return title;
    }

    protected VBox buildForm() {
        VBox form = new VBox(20);

        // Base info + avatar + stats
        HBox baseRow = buildBaseInfoSection();

        // Description
        CharacterDescriptionData characterDescriptionData = new CharacterDescriptionData(character.getDescription(),
                character.getPersonality(),
                character.getBackstory());
        descriptionSection = new CharacterDescriptionSection(FormMode.EDIT, characterDescriptionData);

        // Buffs, Inventory, Skills
        buffEditor = new BuffEditor(character);
        inventoryEditor = new InventoryEditor(character);
        skillsEditor = new SkillsEditor(character);

        form.getChildren().addAll(
                new SectionBox(baseRow),
                new SectionBox(descriptionSection),
                new SectionBox(buffEditor),
                new SectionBox(inventoryEditor),
                new SectionBox(skillsEditor)
        );

        Button saveButton = AppButtonFactory.actionSave(I18n.t("button.editSave"));
        saveButton.setOnAction(event -> saveAndClose());

        Button exitButton = AppButtonFactory.actionExit(I18n.t("button.exit"), 80);
        exitButton.setOnAction(event -> closeScreen());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        buttonBox.getChildren().addAll(exitButton, saveButton);

        form.getChildren().add(buttonBox);

        return form;
    }

    private void closeScreen() {
        StartScreen startScreen = new StartScreen(stage, storageService);
        ScreenManager.setScreen(stage, startScreen.getView());
    }

    private HBox buildBaseInfoSection() {
        HBox row = new HBox(20);
        row.setPadding(new Insets(10));

        avatarPicker = new AvatarPicker(character);

        BaseInfoData baseInfoData = new BaseInfoData(character.getName(),
                character.getRace(),
                character.getCharacterClass(),
                character.getHp(),
                character.getArmor(),
                character.getMaxMana(),
                character.getLevel());
        baseInfoForm = new BaseInfoForm(FormMode.EDIT, baseInfoData);

        Stats stats = character.getStats();
        statsEditor = new StatsEditor(stats, FormMode.EDIT);

        VBox statsSection = new VBox(10);
        statsSection.setPadding(new Insets(12));
        statsSection.setStyle("""
                    -fx-background-color: %s;
                    -fx-background-radius: 8;
                    -fx-border-radius: 8;
                    -fx-border-color: %s;
                """.formatted(AppTheme.BACKGROUND_SECONDARY, AppTheme.BORDER_MUTED));

        statsSection.getChildren().addAll(statsEditor);

        HBox statsContainer = new HBox(statsSection);
        statsContainer.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(statsContainer, Priority.ALWAYS);

        row.getChildren().addAll(avatarPicker, baseInfoForm, statsContainer);

        return row;
    }

    private void saveAndClose() {
        // Base info
        character.setName(baseInfoForm.getData().name());
        character.setRace(baseInfoForm.getData().race());
        character.setCharacterClass(baseInfoForm.getData().characterClass());
        character.setHp(baseInfoForm.getData().hp());
        character.setArmor(baseInfoForm.getData().armor());
        character.setMaxMana(baseInfoForm.getData().mana());
        character.setCurrentMana(baseInfoForm.getData().mana());
        character.setLevel(baseInfoForm.getData().level());
        character.setAvatarImage(avatarPicker.getData().imagePath());

        // Description
        character.setDescription(descriptionSection.getData().description());
        character.setPersonality(descriptionSection.getData().personality());
        character.setBackstory(descriptionSection.getData().backstory());

        // Stats are already linked in statsEditor
        statsEditor.applyTo(character);

        // Buffs, Inventory, Skills
        buffEditor.applyTo(character);
        inventoryEditor.applyTo(character);
        skillsEditor.applyTo(character);

        // Save
        storageService.saveCharacter(character);

        StartScreen startScreen = new StartScreen(stage, storageService);
        ScreenManager.setScreen(stage, startScreen.getView());
    }
}