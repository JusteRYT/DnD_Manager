package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.function.Consumer;

public class InventoryPanel extends VBox {

    private static final int ITEM_CELL_SIZE = 70;

    private final Character character;
    private final FlowPane itemsPane;
    private final InventoryPanelController controller;

    public InventoryPanel(Character character, Consumer<Character> onCharacterUpdated) {
        this.character = character;
        this.controller = new InventoryPanelController(character, onCharacterUpdated);
        setSpacing(6);

        Label title = new Label(I18n.t("inventoryPanel.title"));
        title.setStyle("-fx-text-fill: #c89b3c; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button addBtn = AppButtonFactory.createValueAdjustButton(true, 24, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);
        addBtn.setOnAction(e -> {
            Stage owner = (Stage) addBtn.getScene().getWindow();
            controller.openCreateDialog(owner, this::addItem);
        });

        HBox header = new HBox(8, title, addBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox.setMargin(title, new Insets(0, 0, 4, 0));
        itemsPane = new FlowPane(5, 5);
        itemsPane.setPadding(new Insets(4));
        itemsPane.setPrefWrapLength(ITEM_CELL_SIZE * 6);

        character.getInventory().forEach(this::addItem);

        String goldPrimary = "#c89b3c";
        String commonStyle = """
            -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
            -fx-background-radius: 10;
            -fx-border-color: %s;
            -fx-border-radius: 10;
            -fx-border-width: 1;
            -fx-padding: 8;
            """.formatted(goldPrimary);

        // Базовое состояние
        String idleStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.15), 15, 0, 0, 0);";

        // Состояние при наведении
        String hoverStyle = commonStyle + "-fx-effect: dropshadow(three-pass-box, %s, 10, 0.2, 0, 0);".formatted(goldPrimary);

        this.setStyle(idleStyle);

        this.setOnMouseEntered(e -> this.setStyle(hoverStyle));
        this.setOnMouseExited(e -> this.setStyle(idleStyle));

        getChildren().addAll(header, itemsPane);
    }

    private void addItem(InventoryItem item) {
        itemsPane.getChildren().add(new InventoryItemCell(
                character,
                item,
                this::removeItem,
                this::editItem
        ));
    }

    private void removeItem(InventoryItem item, InventoryItemCell view) {
        controller.removeItem(item, () -> itemsPane.getChildren().remove(view));
    }

    private void editItem(InventoryItem item, InventoryItemCell view) {
        Stage owner = (Stage) view.getScene().getWindow();
        controller.openEditDialog(owner, item, view::refresh);
    }
}
