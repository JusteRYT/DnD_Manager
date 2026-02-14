package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.repository.IconStorageService;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Dialog to add a new inventory item to a character.
 */
public class AddInventoryItemDialog {

    private final Character character;
    private final Consumer<InventoryItem> onItemAddedOrEdited;

    private final InventoryItem existingItem; // null если добавление
    private String iconPath;

    /**
     * Constructor for adding new item
     */
    public AddInventoryItemDialog(Character character, Consumer<InventoryItem> onItemAdded) {
        this(character, null, onItemAdded);
    }

    /**
     * Constructor for editing existing item
     */
    public AddInventoryItemDialog(Character character, InventoryItem itemToEdit, Consumer<InventoryItem> onItemEdited) {
        this.character = character;
        this.existingItem = itemToEdit;
        this.onItemAddedOrEdited = onItemEdited;

        if (existingItem != null) {
            iconPath = existingItem.getIconPath();
        }
    }

    public void show() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(existingItem == null ? "Add Inventory Item" : "Edit Inventory Item");

        AppTextField nameField = new AppTextField(
                existingItem != null ? existingItem.getName() : "Item name"
        );
        AppTextSection descriptionField = new AppTextSection(
                existingItem != null ? existingItem.getDescription() : "", 4, "Description"
        );

        Button iconBtn = AppButtonFactory.customButton("Choose icon", 120);
        iconBtn.setOnAction(e -> iconPath = chooseIcon(stage));

        Button saveBtn = AppButtonFactory.customButton(existingItem == null ? "Add" : "Save", 120);
        saveBtn.setOnAction(e -> {
            if (nameField.getText().isBlank()) return;

            if (existingItem != null) {
                // редактируем текущий объект
                existingItem.setName(nameField.getText());
                existingItem.setDescription(descriptionField.getText());
                existingItem.setIconPath(iconPath != null ? iconPath : "icon/no_image.png");
                onItemAddedOrEdited.accept(existingItem);
            } else {
                // создаём новый объект
                InventoryItem item = new InventoryItem(
                        nameField.getText(),
                        descriptionField.getText(),
                        iconPath != null ? iconPath : "icon/no_image.png"
                );
                character.getInventory().add(item);
                onItemAddedOrEdited.accept(item);
            }

            stage.close();
        });

        VBox root = new VBox(10,
                nameField.getField(),
                descriptionField,
                new HBox(10, iconBtn, saveBtn)
        );
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #1e1e1e;");

        Scene scene = new Scene(root, 420, 260);
        scene.setFill(javafx.scene.paint.Color.web("#1e1e1e"));
        stage.setScene(scene);
        stage.show();
    }

    private String chooseIcon(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg")
        );

        File file = chooser.showOpenDialog(stage);
        if (file == null) return null;

        try {
            IconStorageService storageService = new IconStorageService();
            return storageService.storeIcon(character.getName(), file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}