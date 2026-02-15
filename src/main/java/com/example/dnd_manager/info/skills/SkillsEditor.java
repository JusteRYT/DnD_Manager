package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class SkillsEditor extends VBox {

    @Getter
    private final ObservableList<Skill> skills = FXCollections.observableArrayList();
    private final FlowPane cardsPane = new FlowPane(12, 12);
    private final ObservableList<SkillEffect> currentEffects = FXCollections.observableArrayList();
    private final FlowPane effectsPane = new FlowPane(6, 6);

    private AppTextField effectValueField;
    private AppTextField effectCustomField;
    private AppComboBox<String> effectTypeBox;

    private final Character character;
    private final AppTextSection descriptionSection;

    private final Label nameRequiredLabel = createErrorLabel(I18n.t("labelField.nameRequired"));
    private final Label effectRequiredLabel = createErrorLabel(I18n.t("labelField.effectRequired"));

    public SkillsEditor() {
        this(null);
    }

    public SkillsEditor(Character character) {
        this.character = character;
        setSpacing(15);
        setPadding(new Insets(10));

        // --- Заголовок ---
        Label title = new Label(I18n.t("label.skillsEditor").toUpperCase());
        title.setStyle("-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 13px; -fx-letter-spacing: 1.5px;");

        if (character != null) {
            skills.addAll(character.getSkills());
        }

        // --- Главная карточка ввода ---
        VBox inputCard = new VBox(15);
        inputCard.setStyle("""
                -fx-background-color: linear-gradient(to right, #252526, #1e1e1e);
                -fx-padding: 15;
                -fx-background-radius: 8;
                -fx-border-color: #3a3a3a;
                -fx-border-radius: 8;
                """);

        // --- Имя и активация ---
        AppTextField nameField = new AppTextField(I18n.t("textField.skillName"));

        // NEW: Логика - как только начинаем писать, ошибка пропадает
        nameField.getField().textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) {
                nameRequiredLabel.setVisible(false);
                nameRequiredLabel.setManaged(false);
            }
        });

        AppComboBox<String> activationBox = new AppComboBox<>();
        AtomicReference<String> iconPath = new AtomicReference<>("");
        for (ActivationType type : ActivationType.values()) activationBox.getItems().add(type.getName());
        activationBox.setValue(ActivationType.ACTION.getName());
        activationBox.setPrefWidth(180);

        VBox nameBox = new VBox(0, nameField.getField(), nameRequiredLabel);
        nameBox.setMinHeight(45);
        nameRequiredLabel.setVisible(false);
        nameRequiredLabel.setManaged(false);
        nameRequiredLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 10px; -fx-font-weight: bold;");

        HBox topRow = new HBox(15,
                new VBox(5,
                        createFieldLabel(I18n.t("textFieldLabel.skillName")),
                        nameBox
                ),
                new VBox(5,
                        createFieldLabel(I18n.t("textFieldLabel.activation")),
                        activationBox
                )
        );

        topRow.setViewOrder(-1.0);

        // --- Описание ---
        descriptionSection = new AppTextSection("", 3, I18n.t("textSection.promptText.skillDescription"));
        VBox descBox = new VBox(5, createFieldLabel(I18n.t("textFieldLabel.description")), descriptionSection);

        Label iconPathLabel = new Label();
        iconPathLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 11px;");

        VBox effectsSection = createEnhancedEffectsSection();

        // --- Кнопки ---
        Button iconButton = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        Button addSkillButton = AppButtonFactory.actionSave(I18n.t("button.addSkill"));
        addSkillButton.setPrefWidth(200);

        iconButton.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            File file = chooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                iconPath.set(file.getAbsolutePath());
                iconPathLabel.setText(file.getName());
            }
        });

        addSkillButton.setOnAction(e -> handleAddSkill(nameField, activationBox, iconPathLabel, iconPath));

        HBox settingsRow = new HBox(15, new VBox(5, createFieldLabel(I18n.t("textFieldLabel.iconName")), iconPathLabel));
        HBox buttonsRow = new HBox(15, addSkillButton, iconButton);

        inputCard.getChildren().addAll(topRow, descBox, effectsSection, settingsRow, buttonsRow);

        cardsPane.setPadding(new Insets(10, 0, 0, 0));

        getChildren().addAll(title, inputCard, cardsPane);
        skills.forEach(this::addSkillCard);
    }

    private VBox createEnhancedEffectsSection() {
        effectTypeBox = new AppComboBox<>();
        for (TypeEffects type : TypeEffects.values()) effectTypeBox.getItems().add(type.getName());
        effectTypeBox.setValue(TypeEffects.DAMAGE.getName());

        effectValueField = new AppTextField(I18n.t("textField.promptText.effectValue"));
        effectValueField.getField().textProperty().addListener((obs, old, val) -> {
            if (!val.isEmpty()) {
                effectRequiredLabel.setVisible(false);
                effectRequiredLabel.setManaged(false);
            }
        });

        effectCustomField = new AppTextField(I18n.t("textField.promptText.effectType"));
        effectCustomField.getField().setVisible(false);
        effectCustomField.getField().setManaged(false);

        effectTypeBox.valueProperty().addListener((obs, old, newVal) -> {
            boolean isCustom = Objects.equals(newVal, TypeEffects.CUSTOM.getName());
            effectCustomField.getField().setVisible(isCustom);
            effectCustomField.getField().setManaged(isCustom);
        });

        VBox effectBox = new VBox(0, effectValueField.getField(), effectRequiredLabel);
        effectBox.setMinHeight(45);
        effectBox.setMaxHeight(45);

        Button addEffectBtn = AppButtonFactory.addEffectButton();
        addEffectBtn.setOnAction(e -> handleAddEffect(effectTypeBox, effectCustomField, effectValueField));

        HBox effectInputs = new HBox(8, effectTypeBox, effectCustomField.getField(), effectBox, addEffectBtn);
        effectInputs.setAlignment(Pos.TOP_LEFT);

        HBox.setMargin(addEffectBtn, new Insets(2, 0, 0, 0));
        HBox.setMargin(effectTypeBox, new Insets(2, 0, 0, 0));

        effectInputs.setViewOrder(-1.0);

        VBox section = new VBox(8, createFieldLabel(I18n.t("textFieldLabel.effectsBuilder")), effectInputs, effectsPane);
        section.setStyle("-fx-background-color: rgba(0,0,0,0.2); -fx-padding: 10; -fx-background-radius: 5;");
        return section;
    }

    private Label createErrorLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 10px; -fx-font-weight: bold;");
        label.setVisible(false);
        label.setManaged(false);
        return label;
    }

    private void handleAddEffect(AppComboBox<String> typeBox, AppTextField customField, AppTextField valueField) {
        if (!validateEffect(valueField)) return;

        String typeName = Objects.equals(typeBox.getValue(), TypeEffects.CUSTOM.getName())
                ? customField.getText().trim()
                : typeBox.getValue();
        if (typeName == null || typeName.isEmpty()) return;

        SkillEffect effect = SkillEffect.of(typeBox.getValue(), typeName, valueField.getText().trim());
        currentEffects.add(effect);

        Label tag = new Label(effect.toString());
        tag.setStyle(String.format("""
                -fx-background-color: %s; 
                -fx-text-fill: white; 
                -fx-padding: 3 8; 
                -fx-background-radius: 10; 
                -fx-font-size: 11px; 
                -fx-border-color: rgba(255,255,255,0.2);
                -fx-border-radius: 10;
                """, getEffectColor(typeBox.getValue())));

        effectsPane.getChildren().add(tag);

        valueField.clear();
        customField.clear();
    }

    private void handleAddSkill(AppTextField nameField, AppComboBox<String> activationBox, Label iconLabel, AtomicReference<String> iconPath) {
        if (!effectValueField.getText().trim().isEmpty()) {
            handleAddEffect(effectTypeBox, effectCustomField, effectValueField);
        }

        boolean nameValid = validateName(nameField);
        boolean effectValid = !currentEffects.isEmpty();

        effectRequiredLabel.setVisible(!effectValid);
        effectRequiredLabel.setManaged(!effectValid);

        if (!nameValid || !effectValid) return;

        if (iconLabel.getText().isEmpty()) {
            iconPath.set(getClass().getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm());
        }

        Skill skill = new Skill(
                nameField.getText().trim(),
                descriptionSection.getText(),
                new ArrayList<>(currentEffects),
                activationBox.getValue(),
                iconPath.get()
        );

        skills.add(skill);
        addSkillCard(skill);

        nameField.clear();
        descriptionSection.clear();
        currentEffects.clear();
        effectsPane.getChildren().clear();
        iconPath.set("");
        iconLabel.setText("");
    }

    private void addSkillCard(Skill skill) {
        SkillCard card = new SkillCard(skill, () -> {
            skills.remove(skill);
            cardsPane.getChildren().removeIf(n -> n instanceof SkillCard sc && sc.getSkill() == skill);
        }, character);
        cardsPane.getChildren().add(card);
    }

    private boolean validateName(AppTextField field) {
        boolean valid = !field.getText().isBlank();
        nameRequiredLabel.setVisible(!valid);
        nameRequiredLabel.setManaged(!valid);
        return valid;
    }

    private boolean validateEffect(AppTextField field) {
        boolean valid = !field.getText().isBlank();
        effectRequiredLabel.setVisible(!valid);
        effectRequiredLabel.setManaged(!valid);
        return valid;
    }

    private String getEffectColor(String type) {
        if (type.equals(TypeEffects.DAMAGE.getName())) return "#722f37";
        if (type.equals(TypeEffects.HEAL.getName())) return "#2e5a1c";
        return "#3b444b";
    }

    private Label createFieldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #666; -fx-font-size: 10px; -fx-font-weight: bold;");
        return l;
    }

    public void applyTo(Character character) {
        character.getSkills().clear();
        character.getSkills().addAll(skills);
    }
}