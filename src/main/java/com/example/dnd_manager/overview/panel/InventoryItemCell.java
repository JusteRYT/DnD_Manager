package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.inventory.InventoryItemPopup;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.AppContextMenu;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.util.function.BiConsumer;

public class InventoryItemCell extends StackPane {

    private static final int ITEM_SIZE = 60;
    private static final int ITEM_CELL_SIZE = 70;
    private static final int ICON_SIZE = 130;

    private final Character character;
    private final InventoryItem item;
    private final ImageView icon;
    private final Popup popup;
    private final PauseTransition hoverDelay = new PauseTransition(Duration.millis(200));

    public InventoryItemCell(
            Character character,
            InventoryItem item,
            BiConsumer<InventoryItem, InventoryItemCell> onRemove,
            BiConsumer<InventoryItem, InventoryItemCell> onEdit
    ) {
        this.character = character;
        this.item = item;

        icon = new ImageView();
        icon.setFitWidth(ITEM_SIZE);
        icon.setFitHeight(ITEM_SIZE);
        icon.setPreserveRatio(true);
        icon.setSmooth(true);
        icon.setStyle("-fx-cursor: hand;");

        StackPane container = new StackPane(icon);
        container.setPrefSize(ITEM_CELL_SIZE, ITEM_CELL_SIZE);
        container.setMinSize(ITEM_CELL_SIZE, ITEM_CELL_SIZE);
        container.setMaxSize(ITEM_CELL_SIZE, ITEM_CELL_SIZE);
        container.setStyle("""
                -fx-background-color: #1a1a1a;
                -fx-border-color: #3a3a3a;
                -fx-border-radius: 6;
                -fx-background-radius: 6;
                """);

        container.setOnMouseEntered(e -> container.setStyle(container.getStyle() + "-fx-border-color: #c89b3c; -fx-effect: dropshadow(two-pass-box, rgba(200, 155, 60, 0.3), 5, 0, 0, 0);"));
        container.setOnMouseExited(e -> container.setStyle(container.getStyle().replace("-fx-border-color: #c89b3c;", "-fx-border-color: #3a3a3a;").split("-fx-effect")[0]));
        container.setAlignment(Pos.CENTER);

        getChildren().add(container);

        ContextMenu contextMenu = createContextMenu(onRemove, onEdit);
        container.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(container, e.getScreenX(), e.getScreenY());
            }
        });

        popup = new Popup();
        popup.setAutoHide(true);
        popup.getContent().add(new InventoryItemPopup(item));

        hoverDelay.setOnFinished(e -> {
            if (icon.isHover() && getScene() != null) {
                var bounds = icon.localToScreen(icon.getBoundsInLocal());
                if (!popup.isShowing()) {
                    popup.show(getScene().getWindow(), bounds.getMaxX() + 10, bounds.getMinY());
                }
            }
        });

        icon.hoverProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) hoverDelay.playFromStart();
            else {
                hoverDelay.stop();
                popup.hide();
            }
        });

        refresh();
    }

    private AppContextMenu createContextMenu(BiConsumer<InventoryItem, InventoryItemCell> onRemove,
                                             BiConsumer<InventoryItem, InventoryItemCell> onEdit) {
        AppContextMenu menu = new AppContextMenu();
        menu.addActionItem(I18n.t("button.editItem"), () -> onEdit.accept(item, this));
        menu.addDeleteItem(I18n.t("button.delete"), () -> onRemove.accept(item, this));
        return menu;
    }

    public void refresh() {
        icon.setImage(CharacterAssetResolver.getImage(character, item.getIconPath(), ICON_SIZE, ICON_SIZE));
        icon.setCache(true);
        icon.setCacheHint(CacheHint.QUALITY);
        popup.getContent().clear();
        popup.getContent().add(new InventoryItemPopup(item));
    }
}
