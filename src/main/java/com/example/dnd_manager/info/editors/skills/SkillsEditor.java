package com.example.dnd_manager.info.editors.skills;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.common.AbstractEntityEditor;
import com.example.dnd_manager.info.editors.common.EditorFormLayoutBuilder;
import com.example.dnd_manager.info.editors.common.EditorItemMutationService;
import com.example.dnd_manager.info.editors.common.IconPathDisplayFormatter;
import com.example.dnd_manager.info.skills.view.EffectsBuilderField;
import com.example.dnd_manager.info.skills.model.Skill;
import com.example.dnd_manager.info.skills.view.SkillCard;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.concurrent.atomic.AtomicReference;

public class SkillsEditor extends AbstractEntityEditor<Skill> {

    private Skill editingItem = null;
    private Button addSkillButton;

    // Выносим поля в класс
    private EffectsBuilderField effectsBuilder;
    private AppTextSection descriptionSection;
    private AppTextField nameField;
    private AppComboBox<String> activationBox;
    private final AtomicReference<String> iconPath = new AtomicReference<>("");
    private Label iconPathLabel;
    private final EditorItemMutationService itemMutationService = new EditorItemMutationService();
    private final IconPathDisplayFormatter iconPathDisplayFormatter = new IconPathDisplayFormatter();
    private final SkillEditorItemFactory itemFactory = new SkillEditorItemFactory();
    private final EditorFormLayoutBuilder layoutBuilder = new EditorFormLayoutBuilder(this::createFieldLabel);
    private final SkillsEditorFormBuilder formBuilder = new SkillsEditorFormBuilder(layoutBuilder);

    public SkillsEditor(Character character) {
        super(character, "label.skillsEditor");
    }

    @Override
    protected Pane createItemsContainer() {
        return new FlowPane(12, 12);
    }

    @Override
    protected void loadFromCharacter(Character character) {
        items.addAll(character.getSkills());
    }

    @Override
    protected void fillInputCard(VBox inputCard) {
        SkillsEditorFormControls controls = formBuilder.build(inputCard, nameRequiredLabel);
        effectsBuilder = controls.effectsBuilder();
        nameField = controls.nameField();
        configureNameValidation(nameField);
        activationBox = controls.activationBox();
        descriptionSection = controls.descriptionSection();
        iconPathLabel = controls.iconPathLabel();
        addSkillButton = controls.saveButton();

        AppButtonFactory.attachAssetPicker(controls.assetPickerButton(), path -> {
            iconPath.set(path);
            iconPathLabel.setText(iconPathDisplayFormatter.fileNameOrEmpty(path));
        });

        controls.iconButton().setOnAction(e -> {
            String path = chooseAndImportIcon(AssetCategory.SKILLS);
            if (path != null) {
                iconPath.set(path);
                iconPathLabel.setText(iconPathDisplayFormatter.fileNameOrEmpty(path));
            }
        });

        addSkillButton.setOnAction(e -> handleSave());
    }

    private void handleSave() {
        if (validateName(nameField) && effectsBuilder.validate()) {
            Skill newSkill = itemFactory.create(
                    nameField.getText().trim(),
                    descriptionSection.getText(),
                    effectsBuilder.getEffects(),
                    activationBox.getValue(),
                    resolveIconPath(iconPath)
            );

            itemMutationService.addOrReplace(items, editingItem, newSkill);
            editingItem = null;
            addSkillButton.setText(I18n.t("button.addSkill"));

            refreshUI(); // Метод в базовом классе для перерисовки FlowPane
            clearForm();
        }
    }

    private void prepareEdit(Skill skill) {
        this.editingItem = skill;
        nameField.setText(skill.name());
        descriptionSection.setText(skill.description());
        activationBox.setValue(skill.activationType());

        effectsBuilder.clear();
        skill.effects().forEach(effectsBuilder::addEffect);

        iconPath.set(skill.iconPath());
        iconPathLabel.setText(iconPathDisplayFormatter.fileNameOrEmpty(skill.iconPath()));

        addSkillButton.setText(I18n.t("button.save"));
        nameField.getField().requestFocus();
    }

    private void clearForm() {
        nameField.clear();
        descriptionSection.clear();
        effectsBuilder.clear();
        iconPath.set("");
        iconPathLabel.setText("");
        nameRequiredLabel.setVisible(false);
    }

    @Override
    protected Node createItemRow(Skill skill) {
        return new SkillCard(skill,
                () -> {
                    items.remove(skill);
                    refreshUI();
                },
                () -> prepareEdit(skill),
                character
        );
    }

    @Override
    public void applyTo(Character character) {
        character.getSkills().clear();
        character.getSkills().addAll(items);
    }
}












