package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.dnd_manager.info.skills.TypeEffects.DAMAGE;
import static com.example.dnd_manager.info.skills.TypeEffects.HEAL;

/**
 * Editor component for character skills with multiple effects.
 * Supports CREATE and EDIT modes without directly changing Character.
 */
public class SkillsEditor extends VBox {

    private final ObservableList<Skill> skills = FXCollections.observableArrayList();
    private final FlowPane cardsPane = new FlowPane(10, 10);
    private final ObservableList<SkillEffect> currentEffects = FXCollections.observableArrayList();

    private final FlowPane effectsPane = new FlowPane(6, 6);

    private String iconPath;
    private final Character character;
    private final AppTextSection descriptionSection;

    public SkillsEditor() {
        this(null);
    }

    /**
     * Constructor for EDIT mode with initial skills.
     *
     * @param character pre-filled skills
     */
    public SkillsEditor(Character character) {
        this.character = character;
        setSpacing(12);
        setStyle("-fx-background-color: " + AppTheme.BACKGROUND_SECONDARY + ";");

        if (character != null) {
            skills.addAll(character.getSkills());
        }

        descriptionSection = createDescriptionSection();

        cardsPane.setFocusTraversable(false);
        getChildren().addAll(
                createTitle(),
                createMainControls(),
                descriptionSection,
                createEffectsSection(),
                effectsPane,
                cardsPane
        );

        skills.forEach(this::addSkillCard);
    }

    /**
     * Creates title label.
     */
    private Label createTitle() {
        Label title = new Label(I18n.t("label.skillsEditor"));
        title.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: %s;
                """.formatted(AppTheme.TEXT_ACCENT));
        return title;
    }

    /**
     * Creates main skill input controls.
     */
    private HBox createMainControls() {
        AppTextField nameField = new AppTextField(I18n.t("textField.skillName"));

        AppComboBox<String> activationBox = new AppComboBox<>();
        for (ActivationType activationType : ActivationType.values()) {
            activationBox.getItems().add(activationType.getName());
        }
        activationBox.setValue(ActivationType.ACTION.getName());

        Button iconButton = AppButtonFactory.primary(I18n.t("button.addIcon"));
        iconButton.setOnAction(e -> iconPath = chooseIcon());

        Button addSkillButton = AppButtonFactory.primary(I18n.t("button.addSkill"));
        addSkillButton.setOnAction(e ->
                handleAddSkill(nameField, activationBox)
        );

        return new HBox(10,
                nameField.getField(),
                activationBox,
                iconButton,
                addSkillButton
        );
    }

    /**
     * Creates description section.
     */
    private AppTextSection createDescriptionSection() {
        return new AppTextSection("", 4, I18n.t("textSection.promptText.skillDescription"));
    }

    /**
     * Creates effects editor section.
     */
    private VBox createEffectsSection() {
        AppComboBox<String> effectTypeBox = new AppComboBox<>();
        for (TypeEffects effectType : TypeEffects.values()) {
            effectTypeBox.getItems().add(effectType.getName());
        }
        effectTypeBox.setValue(DAMAGE.getName());

        AppTextField valueField = new AppTextField(I18n.t("textField.promptText.effectValue"));
        AppTextField customTypeField = new AppTextField(I18n.t("textField.promptText.effectType"));

        customTypeField.getField().setVisible(false);
        customTypeField.getField().setManaged(false);

        effectTypeBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isCustom = Objects.equals(newVal, TypeEffects.CUSTOM.getName());
            customTypeField.getField().setVisible(isCustom);
            customTypeField.getField().setManaged(isCustom);
        });

        Button addEffectButton = AppButtonFactory.primary(I18n.t("button.addEffect"));
        addEffectButton.setOnAction(e ->
                handleAddEffect(effectTypeBox, customTypeField, valueField)
        );

        HBox controls = new HBox(8,
                effectTypeBox,
                customTypeField.getField(),
                valueField.getField(),
                addEffectButton
        );

        return new VBox(6, controls);
    }

    /**
     * Handles adding new effect.
     */
    private void handleAddEffect(AppComboBox<String> typeBox,
                                 AppTextField customTypeField,
                                 AppTextField valueField) {

        String value = valueField.getText().trim();
        if (value.isEmpty()) {
            return;
        }

        String type = typeBox.getValue();
        String typeName = Objects.equals(type, TypeEffects.CUSTOM.getName())
                ? customTypeField.getText().trim()
                : type;

        if (Objects.equals(type, TypeEffects.CUSTOM.getName()) && typeName.isEmpty()) {
            return;
        }

        SkillEffect effect = SkillEffect.of(type, typeName, value);
        currentEffects.add(effect);

        Label label = new Label(effect.toString());
        label.setStyle("""
                -fx-padding: 4 8 4 8;
                -fx-border-color: %s;
                -fx-background-color: %s;
                -fx-text-fill: %s;
                """.formatted(
                AppTheme.BORDER_MUTED,
                effectBackground(type),
                AppTheme.TEXT_PRIMARY
        ));

        effectsPane.getChildren().add(label);

        valueField.clear();
        customTypeField.clear();
    }

    /**
     * Handles adding new skill.
     */
    private void handleAddSkill(AppTextField nameField,
                                AppComboBox<String> activationBox) {

        if (nameField.getText().isBlank() || currentEffects.isEmpty()) {
            return;
        }

        Skill skill = new Skill(
                nameField.getText(),
                descriptionSection.getText(),
                new ArrayList<>(currentEffects),
                activationBox.getValue(),
                iconPath
        );

        skills.add(skill);
        addSkillCard(skill);

        nameField.clear();
        currentEffects.clear();
        effectsPane.getChildren().clear();
        descriptionSection.clear();
        iconPath = null;
    }

    /**
     * Adds visual skill card.
     */
    private void addSkillCard(Skill skill) {
        SkillCard card = new SkillCard(
                skill,
                () -> removeSkill(skill),
                character
        );
        cardsPane.getChildren().add(card);
    }

    /**
     * Removes skill from editor.
     */
    private void removeSkill(Skill skill) {
        skills.remove(skill);
        cardsPane.getChildren().removeIf(
                node -> node instanceof SkillCard card && card.getSkill() == skill
        );
    }

    /**
     * Returns effect background color based on effect type.
     */
    private String effectBackground(String type) {
        if (type.equals(DAMAGE.getName())) {
            return "#3b1f1f";
        } else if (type.equals(HEAL.getName())) {
            return "#1f3b2a";
        } else {
            return "#1f2f3b";
        }
    }

    /**
     * Opens file chooser for icon selection.
     */
    private String chooseIcon() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg")
        );
        File file = chooser.showOpenDialog(getScene().getWindow());
        return file != null ? file.getAbsolutePath() : null;
    }

    /**
     * Applies current skills to character.
     */
    public void applyTo(Character character) {
        character.getSkills().clear();
        character.getSkills().addAll(skills);
    }

    /**
     * Returns immutable list of skills.
     */
    public ObservableList<Skill> getSkills() {
        return FXCollections.unmodifiableObservableList(skills);
    }
}