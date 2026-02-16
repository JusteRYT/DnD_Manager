package com.example.dnd_manager.info.editors;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.skills.ActivationType;
import com.example.dnd_manager.info.skills.EffectsBuilderField;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.skills.SkillCard;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
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
        effectsBuilder = new EffectsBuilderField();
        nameField = new AppTextField(I18n.t("textField.skillName"));
        configureNameValidation(nameField);

        activationBox = new AppComboBox<>();
        for (ActivationType type : ActivationType.values()) activationBox.getItems().add(type.getName());
        activationBox.setValue(ActivationType.ACTION.getName());
        activationBox.setPrefWidth(180);

        descriptionSection = new AppTextSection("", 3, I18n.t("textSection.promptText.skillDescription"));

        iconPathLabel = new Label();
        iconPathLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 11px;");

        Button iconButton = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        addSkillButton = AppButtonFactory.actionSave(I18n.t("button.addSkill"));
        addSkillButton.setPrefWidth(200);

        // Layout (topRow и т.д.)
        HBox topRow = new HBox(15,
                new VBox(5, createFieldLabel(I18n.t("textFieldLabel.skillName")),
                        new VBox(0, nameField.getField(), nameRequiredLabel)),
                new VBox(5, createFieldLabel(I18n.t("textFieldLabel.activation")), activationBox)
        );

        inputCard.getChildren().addAll(
                topRow,
                new VBox(5, createFieldLabel(I18n.t("textFieldLabel.description")), descriptionSection),
                effectsBuilder,
                new HBox(15, new VBox(5, createFieldLabel(I18n.t("textFieldLabel.iconName")), iconPathLabel)),
                new HBox(15, addSkillButton, iconButton)
        );

        iconButton.setOnAction(e -> {
            String path = chooseIcon();
            if (path != null) {
                iconPath.set(path);
                iconPathLabel.setText(new File(path).getName());
            }
        });

        addSkillButton.setOnAction(e -> handleSave());
    }

    private void handleSave() {
        if (validateName(nameField) && effectsBuilder.validate()) {
            Skill newSkill = new Skill(
                    nameField.getText().trim(),
                    descriptionSection.getText(),
                    new ArrayList<>(effectsBuilder.getEffects()),
                    activationBox.getValue(),
                    resolveIconPath(iconPath)
            );

            if (editingItem != null) {
                int index = items.indexOf(editingItem);
                if (index != -1) items.set(index, newSkill);
                editingItem = null;
                addSkillButton.setText(I18n.t("button.addSkill"));
            } else {
                items.add(newSkill);
            }

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
        iconPathLabel.setText(skill.iconPath().isEmpty() ? "" : new File(skill.iconPath()).getName());

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