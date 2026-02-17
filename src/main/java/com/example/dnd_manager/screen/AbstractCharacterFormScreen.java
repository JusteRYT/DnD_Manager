package com.example.dnd_manager.screen;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.avatar.AvatarPicker;
import com.example.dnd_manager.info.editors.BuffEditor;
import com.example.dnd_manager.info.editors.InventoryEditor;
import com.example.dnd_manager.info.editors.SkillsEditor;
import com.example.dnd_manager.info.section.FamiliarsSection;
import com.example.dnd_manager.info.stats.StatsEditor;
import com.example.dnd_manager.info.text.BaseInfoForm;
import com.example.dnd_manager.info.text.CharacterDescriptionSection;
import com.example.dnd_manager.info.text.dto.BaseInfoData;
import com.example.dnd_manager.info.text.dto.CharacterDescriptionData;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.SectionBox;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public abstract class AbstractCharacterFormScreen extends AbstractScreen {

    protected final Stage stage;
    protected final StorageService storageService;
    protected final Character character;
    protected final FormMode mode;

    // Компоненты формы
    protected AvatarPicker avatarPicker;
    protected BaseInfoForm baseInfoForm;
    protected StatsEditor statsEditor;
    protected CharacterDescriptionSection descriptionSection;
    protected BuffEditor buffEditor;
    protected InventoryEditor inventoryEditor;
    protected SkillsEditor skillsEditor;
    protected FamiliarsSection familiarsSection;

    public AbstractCharacterFormScreen(Stage stage, StorageService storageService, Character character, FormMode mode) {
        this.stage = stage;
        this.storageService = storageService;
        this.character = character;
        this.mode = mode;
    }

    @Override
    protected VBox buildForm() {
        VBox form = new VBox(30);
        form.setPadding(new Insets(30));
        form.setStyle("-fx-background-color: transparent;");

        // Инициализация компонентов с учетом конструкторов
        initComponents();

        // Сборка интерфейса (общая для обоих экранов)
        HBox heroCard = buildHeroCardSection();
        applyMagicalBorder(heroCard);

        form.getChildren().addAll(
                heroCard,
                wrapInPanel(descriptionSection),
                wrapInPanel(familiarsSection),
                wrapInPanel(buffEditor),
                wrapInPanel(inventoryEditor),
                wrapInPanel(skillsEditor),
                buildActionButtons()
        );

        return form;
    }

    private void initComponents() {
        if (mode == FormMode.CREATE) {
            avatarPicker = new AvatarPicker();
            baseInfoForm = new BaseInfoForm();
            statsEditor = new StatsEditor(character.getStats(), FormMode.CREATE);
            descriptionSection = new CharacterDescriptionSection();
            buffEditor = new BuffEditor(null);
            inventoryEditor = new InventoryEditor(null);
            skillsEditor = new SkillsEditor(null);
            familiarsSection = new FamiliarsSection(stage);
        } else {
            avatarPicker = new AvatarPicker(character);

            BaseInfoData baseData = new BaseInfoData(
                    character.getName(), character.getRace(), character.getCharacterClass(),
                    character.getCurrentHp(), character.getArmor(), character.getMaxMana(), character.getLevel()
            );
            baseInfoForm = new BaseInfoForm(FormMode.EDIT, baseData);

            statsEditor = new StatsEditor(character.getStats(), FormMode.EDIT);

            CharacterDescriptionData descData = new CharacterDescriptionData(
                    character.getDescription(), character.getPersonality(), character.getBackstory()
            );
            descriptionSection = new CharacterDescriptionSection(FormMode.EDIT, descData);

            buffEditor = new BuffEditor(character);
            inventoryEditor = new InventoryEditor(character);
            skillsEditor = new SkillsEditor(character);

            familiarsSection = new FamiliarsSection(stage);
            familiarsSection.getItems().addAll(character.getFamiliars());
            familiarsSection.refreshList();
        }
    }

    private HBox buildHeroCardSection() {
        HBox container = new HBox(10);
        container.setPadding(new Insets(10));

        VBox statsSection = new VBox(15);
        statsSection.setPadding(new Insets(20));
        statsSection.setMinWidth(180);
        statsSection.setAlignment(Pos.TOP_CENTER);
        statsSection.setStyle("-fx-background-color: #252526; -fx-background-radius: 0 8 8 0; -fx-border-color: transparent transparent transparent #333; -fx-border-width: 0 0 0 1;");

        Label titleStats = new Label(I18n.t("stats.label"));
        titleStats.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #FFC107; -fx-border-color: transparent transparent #FFC107 transparent; -fx-border-width: 0 0 1 0;");
        statsSection.getChildren().addAll(titleStats, statsEditor);

        HBox.setHgrow(baseInfoForm, Priority.ALWAYS);
        container.getChildren().addAll(avatarPicker, baseInfoForm, statsSection);
        return container;
    }

    protected Pane wrapInPanel(Node content) {
        SectionBox box = new SectionBox(content);
        applyMagicalBorder(box);
        return box;
    }

    protected void applyMagicalBorder(Node node) {
        node.setStyle(node.getStyle() + "-fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1e1e1e); -fx-background-radius: 12; -fx-border-color: #3a3a3a; -fx-border-radius: 12; -fx-border-width: 1;");
        DropShadow softGlow = new DropShadow(BlurType.THREE_PASS_BOX, Color.web("#ffffff", 0.08), 15, 0, 0, 0);
        node.setEffect(softGlow);
    }

    protected HBox buildActionButtons() {
        Button saveButton = AppButtonFactory.actionSave(getSaveButtonLabel());
        saveButton.setOnAction(event -> handleSave());

        Button exitButton = AppButtonFactory.actionExit(I18n.t("button.exit"), 100);
        exitButton.setOnAction(event -> handleExit());

        HBox buttonBox = new HBox(20, exitButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        return buttonBox;
    }

    protected void syncDataToCharacter() {
        var baseData = baseInfoForm.getData();
        character.setName(baseData.name());
        character.setRace(baseData.race());
        character.setCharacterClass(baseData.characterClass());
        character.setCurrentHp(baseData.hp());
        character.setMaxHp(baseData.hp());
        character.setArmor(baseData.armor());
        character.setMaxMana(baseData.mana());
        character.setCurrentMana(baseData.mana());
        character.setLevel(baseData.level());
        character.setAvatarImage(avatarPicker.getData().imagePath());

        var descData = descriptionSection.getData();
        character.setDescription(descData.description());
        character.setPersonality(descData.personality());
        character.setBackstory(descData.backstory());

        statsEditor.applyTo(character);
        buffEditor.applyTo(character);
        inventoryEditor.applyTo(character);
        skillsEditor.applyTo(character);
        character.getFamiliars().clear();

        if (familiarsSection != null) {
            character.getFamiliars().addAll(familiarsSection.getItems());
        }
    }

    protected abstract String getSaveButtonLabel();
    protected abstract void handleSave();
    protected abstract void handleExit();
}