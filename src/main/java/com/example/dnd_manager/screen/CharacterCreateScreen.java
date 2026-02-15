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

public class CharacterCreateScreen extends AbstractScreen {

    private final Stage stage;
    private final Stats stats = new Stats();
    private final StorageService storageService;

    // Components
    private AvatarPicker avatarPicker;
    private BaseInfoForm baseInfoForm;
    private StatsEditor statsEditor;
    private CharacterDescriptionSection descriptionSection;
    private BuffEditor buffEditor;
    private InventoryEditor inventoryEditor;
    private SkillsEditor skillsEditor;

    public CharacterCreateScreen(Stage stage, StorageService storageService) {
        this.stage = stage;
        this.storageService = storageService;
    }

    @Override
    protected Label buildTitle() {
        Label title = new Label(I18n.t("label.title.create_character"));
        // Эпичный заголовок
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: 900; -fx-text-fill: #c89b3c;");
        BorderPane.setAlignment(title, Pos.CENTER);
        return title;
    }

    @Override
    protected VBox buildForm() {
        VBox form = new VBox(30); // Больше пространства
        form.setPadding(new Insets(30));
        // Общий фон формы (можно сделать прозрачным, если фон родителя уже задан)
        form.setStyle("-fx-background-color: transparent;");

        // Инициализация
        descriptionSection = new CharacterDescriptionSection();
        buffEditor = new BuffEditor();
        inventoryEditor = new InventoryEditor();
        skillsEditor = new SkillsEditor();

        // --- HERO SECTION (Верхняя карточка) ---
        HBox heroCard = buildHeroCardSection();
        applyMagicalBorder(heroCard);

        // --- Other Sections ---
        // Оборачиваем остальные секции в красивые панели
        Pane descBox = wrapInPanel(descriptionSection);
        Pane buffBox = wrapInPanel(buffEditor);
        Pane invBox = wrapInPanel(inventoryEditor);
        Pane skillBox = wrapInPanel(skillsEditor);

        form.getChildren().addAll(
                heroCard,
                descBox,
                buffBox,
                invBox,
                skillBox
        );

        // Кнопки
        HBox buttonBox = buildActionButtons();
        form.getChildren().add(buttonBox);

        return form;
    }

    private Pane wrapInPanel(Node content) {
        SectionBox box = new SectionBox(content); // Предполагаю, что SectionBox - это VBox/Pane
        applyMagicalBorder(box);
        return box;
    }

    private HBox buildHeroCardSection() {
        HBox container = new HBox(10);
        container.setPadding(new Insets(10));

        avatarPicker = new AvatarPicker();
        baseInfoForm = new BaseInfoForm();
        VBox statsSection = buildStatsSection();

        HBox.setHgrow(baseInfoForm, Priority.ALWAYS);
        container.getChildren().addAll(avatarPicker, baseInfoForm, statsSection);
        return container;
    }

    private VBox buildStatsSection() {
        VBox sectionBox = new VBox(15);
        sectionBox.setPadding(new Insets(20));
        sectionBox.setMinWidth(180);
        sectionBox.setAlignment(Pos.TOP_CENTER);

        sectionBox.setStyle("""
                    -fx-background-color: #252526;
                    -fx-background-radius: 0 8 8 0;
                    -fx-border-color: transparent transparent transparent #333;
                    -fx-border-width: 0 0 0 1;
                """);

        Label titleStats = new Label(I18n.t("stats.label"));
        titleStats.setStyle("""
                    -fx-font-size: 14px; 
                    -fx-font-weight: bold; 
                    -fx-text-fill: #FFC107; 
                    -fx-letter-spacing: 2px;
                    -fx-padding: 0 0 5 0;
                    -fx-border-color: transparent transparent #FFC107 transparent;
                    -fx-border-width: 0 0 1 0;
                """);

        statsEditor = new StatsEditor(stats, FormMode.CREATE);
        statsEditor.setStyle("-fx-padding: 10 0 0 0;");

        sectionBox.getChildren().addAll(titleStats, statsEditor);
        return sectionBox;
    }

    private HBox buildActionButtons() {
        Button saveButton = AppButtonFactory.actionSave(I18n.t("button.saveAndView"));
        saveButton.setOnAction(event -> saveAndShowOverview());

        Button exitButton = AppButtonFactory.actionExit(I18n.t("button.exit"), 100);
        exitButton.setOnAction(event -> exitScreen());

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(exitButton, saveButton);
        return buttonBox;
    }

    /**
     * Основной эффект свечения для панелей
     */
    private void applyMagicalBorder(Node node) {
        // Используем стиль из твоего ResourcePanel
        node.setStyle(node.getStyle() + """
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1e1e1e);
                -fx-background-radius: 12;
                -fx-border-color: #3a3a3a;
                -fx-border-radius: 12;
                -fx-border-width: 1;
                """);

        // Твое деликатное свечение
        DropShadow softGlow = new DropShadow();
        softGlow.setBlurType(BlurType.THREE_PASS_BOX);
        softGlow.setColor(Color.web("#ffffff", 0.08));
        softGlow.setRadius(15);
        softGlow.setOffsetX(0);
        softGlow.setOffsetY(0);

        node.setEffect(softGlow);
    }

    // Методы логики (save, exit, getCharacter) остаются без изменений
    private void exitScreen() {
        StartScreen startScreen = new StartScreen(stage, storageService);
        ScreenManager.setScreen(stage, startScreen.getView());
    }

    private void saveAndShowOverview() {
        if (!baseInfoForm.validate()) {
            return;
        }
        Character character = getCharacter();
        character.getStats().copyFrom(stats);
        character.getBuffs().addAll(buffEditor.getBuffs());
        character.getInventory().addAll(inventoryEditor.getItems());
        character.getSkills().addAll(skillsEditor.getSkills());
        storageService.saveCharacter(character);
        CharacterOverviewScreen overviewScreen = new CharacterOverviewScreen(stage, character, storageService);
        ScreenManager.setScreen(stage, overviewScreen);
    }

    private Character getCharacter() {
        Character character = new Character();
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
        character.setDescription(descriptionData.description());
        character.setPersonality(descriptionData.personality());
        character.setBackstory(descriptionData.backstory());
        return character;
    }
}