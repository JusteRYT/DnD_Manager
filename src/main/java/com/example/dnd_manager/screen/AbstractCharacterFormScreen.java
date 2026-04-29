package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.avatar.AvatarPicker;
import com.example.dnd_manager.info.editors.buff.BuffEditor;
import com.example.dnd_manager.info.editors.inventory.InventoryEditor;
import com.example.dnd_manager.info.editors.skills.SkillsEditor;
import com.example.dnd_manager.info.section.FamiliarsSection;
import com.example.dnd_manager.info.stats.editor.StatsEditor;
import com.example.dnd_manager.info.text.BaseInfoForm;
import com.example.dnd_manager.info.text.CharacterDescriptionSection;
import com.example.dnd_manager.screen.form.CharacterCoreFormData;
import com.example.dnd_manager.screen.form.CharacterCoreFormDataApplier;
import com.example.dnd_manager.screen.form.CharacterFormActionButtonsBuilder;
import com.example.dnd_manager.screen.form.CharacterFormComponentFactory;
import com.example.dnd_manager.screen.form.CharacterFormComponents;
import com.example.dnd_manager.screen.form.CharacterFormLayoutBuilder;
import com.example.dnd_manager.screen.form.CharacterFormStyleProvider;
import com.example.dnd_manager.screen.form.CharacterHeroCardSectionBuilder;
import com.example.dnd_manager.screen.form.MagicalBorderDecorator;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public abstract class AbstractCharacterFormScreen extends AbstractScreen {

    protected final Stage stage;
    protected final ScreenNavigator screenNavigator;
    protected final SaveCharacterUseCase saveCharacterUseCase;
    protected final Runnable backToStartAction;
    protected final Character character;
    protected final FormMode mode;
    protected final CharacterCoreFormDataApplier coreFormDataApplier = new CharacterCoreFormDataApplier();
    private final CharacterFormStyleProvider styleProvider = new CharacterFormStyleProvider();
    private final CharacterFormComponentFactory componentFactory = new CharacterFormComponentFactory();
    private final CharacterHeroCardSectionBuilder heroCardSectionBuilder = new CharacterHeroCardSectionBuilder(styleProvider);
    private final CharacterFormActionButtonsBuilder actionButtonsBuilder = new CharacterFormActionButtonsBuilder();
    private final MagicalBorderDecorator magicalBorderDecorator = new MagicalBorderDecorator(styleProvider);
    private final CharacterFormLayoutBuilder layoutBuilder = new CharacterFormLayoutBuilder(styleProvider, magicalBorderDecorator);

    // Компоненты формы
    protected AvatarPicker avatarPicker;
    protected BaseInfoForm baseInfoForm;
    protected StatsEditor statsEditor;
    protected CharacterDescriptionSection descriptionSection;
    protected BuffEditor buffEditor;
    protected InventoryEditor inventoryEditor;
    protected SkillsEditor skillsEditor;
    protected FamiliarsSection familiarsSection;
    protected String originalName;

    public AbstractCharacterFormScreen(
            Stage stage,
            Character character,
            FormMode mode,
            ScreenNavigator screenNavigator,
            SaveCharacterUseCase saveCharacterUseCase,
            Runnable backToStartAction
    ) {
        this.stage = stage;
        this.screenNavigator = Objects.requireNonNull(screenNavigator, "screenNavigator must not be null");
        this.saveCharacterUseCase = Objects.requireNonNull(saveCharacterUseCase, "saveCharacterUseCase must not be null");
        this.backToStartAction = Objects.requireNonNull(backToStartAction, "backToStartAction must not be null");
        this.character = Objects.requireNonNull(character, "character must not be null");
        this.mode = Objects.requireNonNull(mode, "mode must not be null");
        this.originalName = character.getName();
    }

    @Override
    public Parent getView() {
        BorderPane root = new BorderPane();
        root.setStyle(styleProvider.formStyle());

        Node title = buildTitle();
        root.setTop(title);

        VBox form = buildForm();
        form.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        form.setSpacing(15);
        root.setCenter(form);

        return root;
    }

    @Override
    protected VBox buildForm() {
        CharacterFormComponents components = componentFactory.create(stage, character, mode);
        applyComponents(components);
        HBox heroCard = heroCardSectionBuilder.build(avatarPicker, baseInfoForm, statsEditor);
        return layoutBuilder.build(components, heroCard, buildActionButtons());
    }

    private void applyComponents(CharacterFormComponents components) {
        avatarPicker = components.avatarPicker();
        baseInfoForm = components.baseInfoForm();
        statsEditor = components.statsEditor();
        descriptionSection = components.descriptionSection();
        buffEditor = components.buffEditor();
        inventoryEditor = components.inventoryEditor();
        skillsEditor = components.skillsEditor();
        familiarsSection = components.familiarsSection();
    }

    protected HBox buildActionButtons() {
        return actionButtonsBuilder.build(getSaveButtonLabel(), this::handleSave, this::handleExit);
    }

    protected void syncDataToCharacter() {
        var baseData = baseInfoForm.getData();
        var descData = descriptionSection.getData();
        coreFormDataApplier.apply(character, new CharacterCoreFormData(
                baseData.name(),
                baseData.race(),
                baseData.characterClass(),
                baseData.hp(),
                baseData.armor(),
                baseData.mana(),
                baseData.level(),
                avatarPicker.getData().imagePath(),
                descData.description(),
                descData.personality(),
                descData.backstory()
        ));

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

    protected Node buildStyledTitle(String titleText, String subtitleText) {
        javafx.scene.control.Label title = new javafx.scene.control.Label(titleText);
        title.setStyle(styleProvider.screenTitleStyle());

        javafx.scene.control.Label subtitle = new javafx.scene.control.Label(subtitleText);
        subtitle.setStyle(styleProvider.screenSubtitleStyle());

        VBox titleBox = new VBox(2, title, subtitle);
        titleBox.setAlignment(javafx.geometry.Pos.CENTER);
        titleBox.setStyle(styleProvider.titlePanelStyle());
        return titleBox;
    }
}












