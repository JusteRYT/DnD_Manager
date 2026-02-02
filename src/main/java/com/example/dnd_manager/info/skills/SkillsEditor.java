package com.example.dnd_manager.info.skills;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Editor component for character skills.
 */
public class SkillsEditor extends VBox {

    private final ObservableList<Skill> skills = FXCollections.observableArrayList();
    private String iconPath;

    public SkillsEditor() {
        setSpacing(10);

        Label title = new Label("Skills");
        title.setStyle("-fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Skill name");

        TextField damageField = new TextField();
        damageField.setPromptText("Damage (e.g. 1d6)");

        ComboBox<ActivationType> activationBox = new ComboBox<>();
        activationBox.getItems().addAll(ActivationType.values());
        activationBox.setValue(ActivationType.ACTION);

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Skill description");
        descriptionField.setPrefRowCount(2);

        Button iconButton = new Button("Choose icon");
        iconButton.setOnAction(e -> iconPath = chooseIcon());

        FlowPane cardsPane = new FlowPane(10, 10);

        Button addButton = new Button("Add skill");
        addButton.setOnAction(e -> {
            Skill skill = new Skill(
                    nameField.getText(),
                    descriptionField.getText(),
                    damageField.getText(),
                    activationBox.getValue(),
                    iconPath
            );

            skills.add(skill);
            cardsPane.getChildren().add(new SkillCard(skill));

            nameField.clear();
            descriptionField.clear();
            damageField.clear();
            iconPath = null;
        });

        HBox controls = new HBox(10, nameField, damageField, activationBox, iconButton, addButton);

        getChildren().addAll(
                title,
                controls,
                descriptionField,
                cardsPane
        );
    }

    private String chooseIcon() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg")
        );
        File file = chooser.showOpenDialog(getScene().getWindow());
        return file != null ? file.getAbsolutePath() : null;
    }

    public ObservableList<Skill> getSkills() {
        return skills;
    }
}
