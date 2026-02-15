package com.example.dnd_manager.info.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Popup view for inventory item details.
 */
public class InventoryItemPopup extends VBox {

    public InventoryItemPopup(InventoryItem item) {
        setSpacing(6);
        setPadding(new Insets(10));

        setStyle("""
            -fx-background-color: #252526;
            -fx-background-radius: 8;
            -fx-border-color: #3a3a3a;
            -fx-border-radius: 8;
        """);

        Label name = new Label(item.getName());
        name.setStyle("""
            -fx-text-fill: #c89b3c;
            -fx-font-weight: bold;
        """);

        Label count = new Label("Количество: " + item.getCount());
        count.setStyle("""
                    -fx-text-fill: #c89b3c;
                    -fx-font-weight: bold;
                """);

        if (!item.getDescription().isEmpty()) {
            Label description = new Label(item.getDescription());
            description.setWrapText(true);
            description.setStyle("-fx-text-fill: #dddddd;");
            getChildren().addAll(name, count, description);
            return;
        }

        getChildren().addAll(name, count);
    }
}
