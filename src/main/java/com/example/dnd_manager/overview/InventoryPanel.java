package com.example.dnd_manager.overview;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.theme.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;


/**
 * Inventory panel with add/edit/remove support.
 */
public class InventoryPanel extends VBox {

    private final Character character;
    private final FlowPane itemsPane;
    private final Consumer<Character> onCharacterUpdated;

    public InventoryPanel(Character character, Consumer<Character> onCharacterUpdated) {
        this.character = character;
        this.onCharacterUpdated = onCharacterUpdated;
        setSpacing(6);

        Label title = new Label("INVENTORY");
        title.setStyle("""
                -fx-text-fill: #c89b3c;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                """);

        Button addBtn = AppButtonFactory.customButton("+", 24);
        addBtn.setOnAction(e ->
                new AddInventoryItemDialog(character, this::onItemCreated).show()
        );

        HBox header = new HBox(8, title, addBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        itemsPane = new FlowPane(10, 10);
        itemsPane.setPadding(new Insets(8));

        // Инициализация UI
        character.getInventory()
                .forEach(this::addItem);

        setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");
        setPadding(new Insets(8));

        getChildren().addAll(header, itemsPane);
    }

    private void onItemCreated(InventoryItem item) {
        character.getInventory().add(item);
        addItem(item);
        onCharacterUpdated.accept(character);
    }

    /**
     * Adds item to UI.
     */
    private void addItem(InventoryItem item) {
        itemsPane.getChildren().add(
                new InventoryItemView(character, item, this::removeItem, this::editItem)
        );
    }

    /**
     * Removes item from model and UI.
     */
    private void removeItem(InventoryItem item, InventoryItemView view) {
        character.getInventory().remove(item);
        itemsPane.getChildren().remove(view);
        onCharacterUpdated.accept(character);
    }

    /**
     * Opens edit dialog for item using a separate EditInventoryItemDialog.
     */
    private void editItem(InventoryItem item, InventoryItemView view) {
        new EditInventoryItemDialog(character, item, updated -> {
            view.refresh(); // обновляем карточку
            onCharacterUpdated.accept(character); // уведомляем владельца
        }).show();
    }
}