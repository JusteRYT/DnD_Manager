package com.example.dnd_manager.info.inventory;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Custom list cell for inventory items.
 */
public class InventoryCell extends ListCell<InventoryItem> {

    @Override
    protected void updateItem(InventoryItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        ImageView icon = new ImageView();
        icon.setFitWidth(32);
        icon.setFitHeight(32);
        icon.setPreserveRatio(true);

        if (item.getIconPath() != null) {
            icon.setImage(new Image("file:" + item.getIconPath()));
        }

        Label name = new Label(item.getName());
        name.setStyle("-fx-font-weight: bold;");

        Label description = new Label(item.getDescription());
        description.setWrapText(true);

        VBox textBox = new VBox(name, description);
        HBox root = new HBox(10, icon, textBox);

        setGraphic(root);
    }
}
