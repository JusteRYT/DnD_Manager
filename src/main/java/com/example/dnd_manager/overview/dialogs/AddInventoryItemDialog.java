package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.repository.IconStorageService;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class AddInventoryItemDialog extends BaseDialog {

    private final Character character;
    private final Consumer<InventoryItem> onItemAddedOrEdited;
    private final InventoryItem existingItem;
    private String iconPath;

    public AddInventoryItemDialog(Stage owner, Character character, InventoryItem itemToEdit, Consumer<InventoryItem> onComplete) {
        super(owner,
                itemToEdit == null ? "Add Inventory Item" : "Edit Inventory Item",
                450, 320);

        this.character = character;
        this.existingItem = itemToEdit;
        this.onItemAddedOrEdited = onComplete;

        if (existingItem != null) {
            this.iconPath = existingItem.getIconPath();
        }
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(15);

        AppTextField nameField = new AppTextField(
                existingItem != null ? existingItem.getName() : "Item name"
        );
        AppTextSection descriptionField = new AppTextSection(
                existingItem != null ? existingItem.getDescription() : "", 3, "Description"
        );
        AppTextField countField = new AppTextField(
                existingItem != null ? String.valueOf(existingItem.getCount()) : "1"
        );

        Button iconBtn = AppButtonFactory.customButton("Choose icon", 120);
        iconBtn.setOnAction(e -> iconPath = chooseIcon());

        Button saveBtn = AppButtonFactory.customButton(existingItem == null ? "Add" : "Save", 120);
        saveBtn.setOnAction(e -> {
            if (nameField.getText().isBlank()) return;
            saveData(nameField.getText(), descriptionField.getText(), countField.getText());
            close();
        });

        HBox actions = new HBox(10, iconBtn, saveBtn);
        contentArea.getChildren().addAll(
                nameField.getField(),
                descriptionField,
                countField.getField(),
                actions
        );
    }

    private void saveData(String name, String desc, String countStr) {
        int count = 0;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException ignored) {
        }

        if (existingItem != null) {
            existingItem.setName(name);
            existingItem.setDescription(desc);
            existingItem.setCount(count);
            existingItem.setIconPath(iconPath != null ? iconPath : "icon/no_image.png");
            onItemAddedOrEdited.accept(existingItem);
        } else {
            InventoryItem item = new InventoryItem(name, desc, iconPath != null ? iconPath : "icon/no_image.png");
            item.setCount(count);
            character.getInventory().add(item);
            onItemAddedOrEdited.accept(item);
        }
    }

    private String chooseIcon() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg"));
        File file = chooser.showOpenDialog(stage);
        if (file == null) return null;

        try {
            return new IconStorageService().storeIcon(character.getName(), file);
        } catch (IOException e) {
            return null;
        }
    }
}