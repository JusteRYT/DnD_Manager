package com.example.dnd_manager.info.inventory;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * UI row representing a single inventory item.
 */
@Getter
public class InventoryRow extends HBox {

    private final InventoryItem item;

    /**
     * @param item inventory item model
     * @param onDelete delete callback
     */
    public InventoryRow(InventoryItem item, Runnable onDelete, Character character) {
        this.item = item;

        setSpacing(10);
        setStyle("""
            -fx-background-color: %s;
            -fx-background-radius: 6;
            -fx-padding: 8;
        """.formatted(AppTheme.BACKGROUND_SECONDARY));

        ImageView icon = new ImageView();
        icon.setFitWidth(32);
        icon.setFitHeight(32);
        icon.setPreserveRatio(true);
        icon.setImage(chooseIcon(item, character));


        Label name = new Label(item.getName());
        name.setStyle("""
            -fx-font-weight: bold;
            -fx-text-fill: %s;
        """.formatted(AppTheme.TEXT_PRIMARY));

        Label description = new Label(item.getDescription());
        description.setWrapText(true);
        description.setStyle("""
            -fx-text-fill: %s;
            -fx-font-size: 12px;
        """.formatted(AppTheme.TEXT_PRIMARY));

        VBox textBox = new VBox(4, name, description);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        var deleteButton = AppButtonFactory.customButton("✕", 35, AppTheme.BUTTON_REMOVE, AppTheme.BUTTON_REMOVE_HOVER);
        deleteButton.setOnAction(e -> onDelete.run());
        deleteButton.setFocusTraversable(false);

        getChildren().addAll(icon, textBox, deleteButton);
    }

    private Image chooseIcon(InventoryItem item, Character character) {
        Image image = null;
        if (item.getIconPath() != null && !item.getIconPath().isEmpty() && character != null) {
            image = new Image(CharacterAssetResolver.resolve(character.getName(), item.getIconPath()));
        } else {
            if (item.getIconPath() != null && !item.getIconPath().isEmpty()) {
                image = new Image("file:" + item.getIconPath());
            }
        }

        return image;
    }
}