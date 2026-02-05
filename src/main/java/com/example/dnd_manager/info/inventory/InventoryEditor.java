package com.example.dnd_manager.info.inventory;

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
import lombok.Getter;

import java.io.File;

/**
 * Editor component for character inventory.
 */
public class InventoryEditor extends VBox {

    @Getter
    private final ObservableList<InventoryItem> items = FXCollections.observableArrayList();
    private final VBox listContainer = new VBox(6);

    private String iconPath;

    public InventoryEditor() {
        setSpacing(10);

        Label title = new Label("Inventory");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #c89b3c");

        AppTextField nameField = new AppTextField("Item name");
        AppTextSection descriptionTextSection = new AppTextSection("", 2, "Description");

        Button iconButton = AppButtonFactory.customButton("Add Icon", 100);
        iconButton.setOnAction(e -> iconPath = chooseIcon());

        Button addButton = AppButtonFactory.customButton("Add Item", 100);
        addButton.setOnAction(event -> {
            InventoryItem item = new InventoryItem(
                    nameField.getText(),
                    descriptionTextSection.getText(),
                    iconPath
            );

            items.add(item);

            InventoryRow row = new InventoryRow(item, () -> removeItemRow(item));
            listContainer.getChildren().add(row);

            nameField.setText("");
            descriptionTextSection.setText("");
            iconPath = null;
        });

        HBox controls = new HBox(10, nameField.getField(), iconButton, addButton);

        getChildren().addAll(
                title,
                controls,
                descriptionTextSection,
                listContainer
        );
    }

    private void removeItemRow(InventoryItem item) {
        listContainer.getChildren().stream()
                .filter(node -> node instanceof InventoryRow row && row.getItem() == item)
                .findFirst()
                .ifPresent(node -> {
                    node.setManaged(false);
                    node.setVisible(false);
                    items.remove(item);
                });
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
