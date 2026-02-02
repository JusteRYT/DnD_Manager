package com.example.dnd_manager.info.buff_debuff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Editor component for character buffs and debuffs.
 */
public class BuffEditor extends VBox {

    private final ObservableList<Buff> buffs = FXCollections.observableArrayList();
    private final VBox listContainer = new VBox(5);

    public BuffEditor() {
        setSpacing(10);

        Label title = new Label("Buffs / Debuffs");
        title.setStyle("-fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description");
        descriptionField.setPrefRowCount(2);

        ComboBox<BuffType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(BuffType.values());
        typeBox.setValue(BuffType.BUFF);

        Button addButton = new Button("Add");

        addButton.setOnAction(event -> {
            Buff buff = new Buff(
                    nameField.getText(),
                    descriptionField.getText(),
                    typeBox.getValue()
            );

            buffs.add(buff);
            listContainer.getChildren().add(new BuffEditorRow(buff));

            nameField.clear();
            descriptionField.clear();
        });

        HBox controls = new HBox(10, nameField, typeBox, addButton);
        getChildren().addAll(
                title,
                controls,
                descriptionField,
                listContainer
        );
    }

    /**
     * @return list of created buffs
     */
    public ObservableList<Buff> getBuffs() {
        return buffs;
    }
}
