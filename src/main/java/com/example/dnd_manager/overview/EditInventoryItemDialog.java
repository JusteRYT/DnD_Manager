package com.example.dnd_manager.overview;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.repository.IconStorageService;
import com.example.dnd_manager.theme.AppButtonFactory;
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
 * Dialog to edit an existing inventory item.
 */
public class EditInventoryItemDialog {

    private final Character character;
    private final InventoryItem item;
    private final Consumer<InventoryItem> onItemEdited;

    private String iconPath;

    public EditInventoryItemDialog(Character character, InventoryItem item, Consumer<InventoryItem> onItemEdited) {
        this.character = character;
        this.item = item;
        this.onItemEdited = onItemEdited;
        this.iconPath = item.getIconPath();
    }

    public void show() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Inventory Item");

        AppTextField nameField = new AppTextField(item.getName());
        AppTextSection descriptionField = new AppTextSection(item.getDescription(), 4, "Description");

        Button iconBtn = AppButtonFactory.customButton("Choose icon", 120);
        iconBtn.setOnAction(e -> iconPath = chooseIcon(stage));

        Button saveBtn = AppButtonFactory.customButton("Save", 120);
        saveBtn.setOnAction(e -> {
            if (nameField.getText().isBlank()) return;

            item.setName(nameField.getText());
            item.setDescription(descriptionField.getText());
            item.setIconPath(iconPath != null ? iconPath : "icon/images.png");

            onItemEdited.accept(item);
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
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg"));

        File file = chooser.showOpenDialog(stage);
        if (file == null) return iconPath;

        try {
            IconStorageService storageService = new IconStorageService();
            return storageService.storeIcon(character.getName(), file);
        } catch (IOException e) {
            e.printStackTrace();
            return iconPath;
        }
    }
}
