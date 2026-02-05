package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.Getter;

import java.io.File;

/**
 * Editor component for character skills.
 */
public class SkillsEditor extends VBox {

    @Getter
    private final ObservableList<Skill> skills = FXCollections.observableArrayList();
    private String iconPath;

    public SkillsEditor() {
        setSpacing(10);

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

        FlowPane cardsPane = new FlowPane(10, 10);

        Button addButton = AppButtonFactory.customButton("Add skill", 100);
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

        HBox controls = new HBox(10, nameField.getField(), damageField.getField(), activationBox, iconButton, addButton);

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

}
