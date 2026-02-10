package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.domain.Character;
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
        setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + ";");

        if (character != null) {
            skills.addAll(character.getSkills());
        }

        getChildren().addAll(
                createTitle(),
                createMainControls(),
                createDescriptionSection(),
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
        Label title = new Label("Skills");
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
        AppTextField nameField = new AppTextField("Skill name");

        AppComboBox<ActivationType> activationBox = new AppComboBox<>();
        activationBox.getItems().addAll(ActivationType.values());
        activationBox.setValue(ActivationType.ACTION);

        Button iconButton = AppButtonFactory.customButton("Choose icon", 110);
        iconButton.setOnAction(e -> iconPath = chooseIcon());

        Button addSkillButton = AppButtonFactory.customButton("Add skill", 110);
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
        return new AppTextSection("", 4, "Skill description");
    }

    /**
     * Creates effects editor section.
     */
    private VBox createEffectsSection() {
        AppComboBox<TypeEffects> effectTypeBox = new AppComboBox<>();
        effectTypeBox.getItems().addAll(TypeEffects.values());
        effectTypeBox.setValue(TypeEffects.DAMAGE);

        AppTextField valueField = new AppTextField("Value (1d6, 5, +2)");
        AppTextField customTypeField = new AppTextField("Custom type");

        customTypeField.getField().setVisible(false);
        customTypeField.getField().setManaged(false);

        effectTypeBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isCustom = newVal == TypeEffects.CUSTOM;
            customTypeField.getField().setVisible(isCustom);
            customTypeField.getField().setManaged(isCustom);
        });

        Button addEffectButton = AppButtonFactory.customButton("Add effect", 110);
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
    private void handleAddEffect(AppComboBox<TypeEffects> typeBox,
                                 AppTextField customTypeField,
                                 AppTextField valueField) {

        String value = valueField.getText().trim();
        if (value.isEmpty()) {
            return;
        }

        TypeEffects type = typeBox.getValue();
        String typeName = type == TypeEffects.CUSTOM
                ? customTypeField.getText().trim()
                : type.name();

        if (type == TypeEffects.CUSTOM && typeName.isEmpty()) {
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
                                AppComboBox<ActivationType> activationBox) {

        if (nameField.getText().isBlank() || currentEffects.isEmpty()) {
            return;
        }

        Skill skill = new Skill(
                nameField.getText(),
                "",
                new ArrayList<>(currentEffects),
                activationBox.getValue(),
                iconPath
        );

        skills.add(skill);
        addSkillCard(skill);

        nameField.clear();
        currentEffects.clear();
        effectsPane.getChildren().clear();
        iconPath = null;
    }

    /**
     * Adds visual skill card.
     */
    private void addSkillCard(Skill skill) {
        SkillCard card = new SkillCard(
                skill,
                () -> removeSkill(skill),
                character != null ? character.getName() : "unknown"
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
    private String effectBackground(TypeEffects type) {
        return switch (type) {
            case DAMAGE -> "#3b1f1f";
            case HEAL -> "#1f3b2a";
            default -> "#1f2f3b";
        };
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
        character.getSkills().addAll(skills);
    }

    /**
     * Returns immutable list of skills.
     */
    public ObservableList<Skill> getSkills() {
        return FXCollections.unmodifiableObservableList(skills);
    }
}