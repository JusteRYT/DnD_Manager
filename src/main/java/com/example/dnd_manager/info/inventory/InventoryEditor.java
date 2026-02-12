package com.example.dnd_manager.info.inventory;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Editor component for character inventory.
 * Supports CREATE and EDIT modes without напрямую менять Character.
 */
public class InventoryEditor extends VBox {

    private final ObservableList<InventoryItem> items = FXCollections.observableArrayList();
    private final VBox listContainer = new VBox(6);
    private final Character character;

    private String iconPath;

    public InventoryEditor() {
        this(null);
    }

    /**
     * Constructor for EDIT mode with initial items.
     *
     * @param character character
     */
    public InventoryEditor(Character character) {
        this.character = character;
        setSpacing(10);

        Label title = new Label(I18n.t("label.inventoryEditor"));
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #c89b3c");

        if (character != null) {
            items.addAll(character.getInventory());
        }

        AppTextField nameField = new AppTextField(I18n.t("textField.inventoryName"));
        AppTextSection descriptionTextSection = new AppTextSection("", 4, I18n.t("textSection.inventoryDescription"));

        Button iconButton = AppButtonFactory.primary(I18n.t("button.addIcon"));
        iconButton.setOnAction(e -> iconPath = chooseIcon());

        Button addButton = AppButtonFactory.primary(I18n.t("button.addItem"));
        addButton.setOnAction(event -> {
            InventoryItem item = new InventoryItem(
                    nameField.getText(),
                    descriptionTextSection.getText(),
                    iconPath
            );

            addItem(item);

            nameField.setText("");
            descriptionTextSection.setText("");
            iconPath = null;
        });

        HBox controls = new HBox(10, nameField.getField(), iconButton, addButton);

        getChildren().addAll(title, controls, descriptionTextSection, listContainer);

        // Создаем строки для initialItems
        for (InventoryItem item : items) {
            addItemRow(item);
        }
    }

    private void addItem(InventoryItem item) {
        items.add(item);
        addItemRow(item);
    }

    private void addItemRow(InventoryItem item) {
        InventoryRow row = new InventoryRow(item, () -> removeItem(item), character);
        listContainer.getChildren().add(row);
    }

    private void removeItem(InventoryItem item) {
        listContainer.getChildren().removeIf(node -> node instanceof InventoryRow row && row.getItem() == item);
        items.remove(item);
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
     * Apply inventory items to Character object.
     */
    public void applyTo(Character character) {
        character.getInventory().clear();
        character.getInventory().addAll(items);
    }

    /**
     * Returns unmodifiable list of current items.
     */
    public ObservableList<InventoryItem> getItems() {
        return FXCollections.unmodifiableObservableList(items);
    }
}