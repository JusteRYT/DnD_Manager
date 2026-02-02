package com.example.dnd_manager.info.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Editor component for character inventory.
 */
public class InventoryEditor extends VBox {

    private final ObservableList<InventoryItem> items = FXCollections.observableArrayList();
    private String iconPath;

    public InventoryEditor() {
        setSpacing(10);

        Label title = new Label("Inventory");
        title.setStyle("-fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Item name");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Item description");
        descriptionField.setPrefRowCount(2);

        Button iconButton = new Button("Choose icon");
        iconButton.setOnAction(e -> iconPath = chooseIcon());

        Button addButton = new Button("Add item");
        addButton.setOnAction(event -> {
            InventoryItem item = new InventoryItem(
                    nameField.getText(),
                    descriptionField.getText(),
                    iconPath
            );
            items.add(item);
            nameField.clear();
            descriptionField.clear();
            iconPath = null;
        });

        ListView<InventoryItem> listView = new ListView<>(items);
        listView.setCellFactory(v -> new InventoryCell());

        HBox controls = new HBox(10, nameField, iconButton, addButton);

        getChildren().addAll(
                title,
                controls,
                descriptionField,
                listView
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

    public ObservableList<InventoryItem> getItems() {
        return items;
    }
}
