package com.example.dnd_manager.overview;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.inventory.InventoryItemPopup;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;

import java.util.function.BiConsumer;

/**
 * UI representation of inventory item with popup and context menu.
 */
public class InventoryItemView extends StackPane {

    private static final String DEFAULT_ICON =
            "/com/example/dnd_manager/icon/images.png";

    private final Character character;
    private final InventoryItem item;
    private final ImageView icon;

    private Popup popup;

    public InventoryItemView(
            Character character,
            InventoryItem item,
            BiConsumer<InventoryItem, InventoryItemView> onRemove,
            BiConsumer<InventoryItem, InventoryItemView> onEdit
    ) {
        this.character = character;
        this.item = item;

        this.icon = new ImageView();
        icon.setFitWidth(60);
        icon.setFitHeight(60);
        icon.setPreserveRatio(true);
        icon.setStyle("-fx-cursor: hand;");

        refresh();
        setupPopup();
        setupContextMenu(onEdit, onRemove);

        getChildren().add(icon);
    }

    /**
     * Creates tooltip popup.
     */
    private void setupPopup() {
        popup = new Popup();
        popup.setAutoHide(true);
        popup.getContent().add(new InventoryItemPopup(item));

        icon.setOnMouseEntered(e -> {
            var b = icon.localToScreen(icon.getBoundsInLocal());
            popup.show(getScene().getWindow(), b.getMaxX() + 10, b.getMinY());
        });

        icon.setOnMouseExited(e -> popup.hide());
    }

    /**
     * Creates dark themed context menu.
     */
    private void setupContextMenu(
            BiConsumer<InventoryItem, InventoryItemView> onEdit,
            BiConsumer<InventoryItem, InventoryItemView> onRemove
    ) {
        ContextMenu menu = new ContextMenu();
        menu.setStyle("""
                -fx-background-color: #1e1e1e;
                -fx-background-radius: 6;
                -fx-border-color: #3a3a3a;
                -fx-border-radius: 6;
                """);

        MenuItem edit = new MenuItem("Edit");
        MenuItem remove = new MenuItem("Delete");

        styleMenuItem(edit);
        styleMenuItem(remove);

        edit.setOnAction(e -> onEdit.accept(item, this));
        remove.setOnAction(e -> onRemove.accept(item, this));

        menu.getItems().addAll(edit, remove);

        icon.setOnContextMenuRequested(e ->
                menu.show(icon, e.getScreenX(), e.getScreenY())
        );
    }

    private void styleMenuItem(MenuItem item) {
        item.setStyle("""
                -fx-text-fill: #dddddd;
                -fx-background-color: transparent;
                """);

        item.setOnMenuValidation(e ->
                item.setStyle("-fx-text-fill: #dddddd;"));

        item.setOnAction(e ->
                item.setStyle("-fx-text-fill: #c89b3c;"));
    }

    /**
     * Refreshes icon and popup after edit.
     */
    public void refresh() {
        icon.setImage(loadIcon());

        if (popup != null) {
            popup.hide();
            popup.getContent().clear();
            popup.getContent().add(new InventoryItemPopup(item));
        }
    }


    private Image loadIcon() {
        if (item.getIconPath() == null || item.getIconPath().isBlank()) {
            return new Image(
                    getClass().getResource(DEFAULT_ICON).toExternalForm()
            );
        }

        return new Image(
                CharacterAssetResolver.resolve(character.getName(), item.getIconPath())
        );
    }
}
