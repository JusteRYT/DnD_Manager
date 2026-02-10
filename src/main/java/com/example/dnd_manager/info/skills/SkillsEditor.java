package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Editor component for character skills.
 * Supports CREATE and EDIT modes without напрямую менять Character.
 */
public class SkillsEditor extends VBox {

    private final ObservableList<Skill> skills = FXCollections.observableArrayList();
    private final FlowPane cardsPane = new FlowPane(10, 10);

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
        setSpacing(10);

        if (character != null) {
            skills.addAll(character.getSkills());
        }

        Label title = new Label("Skills");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #c89b3c");

        AppTextField nameField = new AppTextField("Skill name");
        AppTextField damageField = new AppTextField("Damage (e.g. 1d6)");

        AppComboBox<ActivationType> activationBox = new AppComboBox<>();
        activationBox.getItems().addAll(ActivationType.values());
        activationBox.setValue(ActivationType.ACTION);

        AppTextSection descriptionField = new AppTextSection("", 4, "Skill description");

        Button iconButton = AppButtonFactory.customButton("Choose icon", 100);
        iconButton.setOnAction(e -> iconPath = chooseIcon());

        Button addButton = AppButtonFactory.customButton("Add skill", 100);
        addButton.setOnAction(e -> {
            Skill skill = new Skill(
                    nameField.getText(),
                    descriptionField.getText(),
                    damageField.getText(),
                    activationBox.getValue(),
                    iconPath
            );

            addSkill(skill);

            nameField.clear();
            damageField.clear();
            descriptionField.clear();
            iconPath = null;
        });

        HBox controls = new HBox(
                10,
                nameField.getField(),
                damageField.getField(),
                activationBox,
                iconButton,
                addButton
        );

        getChildren().addAll(title, controls, descriptionField, cardsPane);

        // Создаем карточки для initialSkills
        for (Skill skill : skills) {
            addSkillCard(skill);
        }
    }

    private void addSkill(Skill skill) {
        skills.add(skill);
        addSkillCard(skill);
    }

    private void addSkillCard(Skill skill) {
        SkillCard card = new SkillCard(skill, () -> removeSkill(skill), character.getName());
        cardsPane.getChildren().add(card);
    }

    private void removeSkill(Skill skill) {
        cardsPane.getChildren().removeIf(node -> node instanceof SkillCard card && card.getSkill() == skill);
        skills.remove(skill);
    }

    private String chooseIcon() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg")
        );
        File file = chooser.showOpenDialog(getScene().getWindow());
        return file != null ? file.getAbsolutePath() : null;
    }

    /**
     * Apply skills to Character object.
     */
    public void applyTo(Character character) {
        character.getSkills().clear();
        character.getSkills().addAll(skills);
    }

    /**
     * Returns unmodifiable list of current skills.
     */
    public ObservableList<Skill> getSkills() {
        return FXCollections.unmodifiableObservableList(skills);
    }
}