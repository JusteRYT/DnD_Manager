package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.inventory.InventoryItemPopup;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.AddInventoryItemDialog;
import com.example.dnd_manager.overview.dialogs.EditInventoryItemDialog;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.AppContextMenu;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class InventoryPanel extends VBox {

    private static final int ITEM_SIZE = 60;
    private static final int ITEM_CELL_SIZE = 70;

    private final Character character;
    private final FlowPane itemsPane;
    private final Consumer<Character> onCharacterUpdated;

    public InventoryPanel(Character character, Consumer<Character> onCharacterUpdated) {
        this.character = character;
        this.onCharacterUpdated = onCharacterUpdated;
        setSpacing(6);

        Label title = new Label(I18n.t("inventoryPanel.title"));
        title.setStyle("-fx-text-fill: #c89b3c; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button addBtn = AppButtonFactory.createValueAdjustButton(true, 24, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);
        addBtn.setOnAction(e -> {
            Stage owner = (Stage) addBtn.getScene().getWindow();
            new AddInventoryItemDialog(owner, character, null, this::onItemCreated).show();
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

    private void onItemCreated(InventoryItem item) {
        addItem(item);
        onCharacterUpdated.accept(character);
    }

    private void addItem(InventoryItem item) {
        itemsPane.getChildren().add(new InventoryItemCell(character, item, this::removeItem, this::editItem));
    }

    private void removeItem(InventoryItem item, InventoryItemCell view) {
        character.getInventory().remove(item);
        itemsPane.getChildren().remove(view);
        onCharacterUpdated.accept(character);
    }

    private void editItem(InventoryItem item, InventoryItemCell view) {
        Stage owner = (Stage) view.getScene().getWindow();
        new EditInventoryItemDialog(owner, character, item, updated -> {
            view.refresh();
            if (onCharacterUpdated != null) {
                onCharacterUpdated.accept(character);
            }
        }).show();
    }

    private static class InventoryItemCell extends StackPane {

        private final Character character;
        private final InventoryItem item;
        private final ImageView icon;
        private final Popup popup;
        private final PauseTransition hoverDelay = new PauseTransition(Duration.millis(200));

        public InventoryItemCell(Character character, InventoryItem item,
                                 BiConsumer<InventoryItem, InventoryItemCell> onRemove,
                                 BiConsumer<InventoryItem, InventoryItemCell> onEdit) {
            this.character = character;
            this.item = item;

            icon = new ImageView();
            icon.setFitWidth(ITEM_SIZE);
            icon.setFitHeight(ITEM_SIZE);
            icon.setPreserveRatio(false);
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

            // Подсветка при наведении
            container.setOnMouseEntered(e -> container.setStyle(container.getStyle() + "-fx-border-color: #c89b3c; -fx-effect: dropshadow(two-pass-box, rgba(200, 155, 60, 0.3), 5, 0, 0, 0);"));
            container.setOnMouseExited(e -> container.setStyle(container.getStyle().replace("-fx-border-color: #c89b3c;", "-fx-border-color: #3a3a3a;").split("-fx-effect")[0]));
            container.setAlignment(Pos.CENTER);

            getChildren().add(container);

            // === КОНТЕКСТНОЕ МЕНЮ (ПКМ) ===
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
            menu.addActionItem(I18n.t("button.edit"), () -> onEdit.accept(item, this));
            menu.addDeleteItem(I18n.t("button.delete"), () -> onRemove.accept(item, this));

            return menu;
        }

        public void refresh() {
            if (item.getIconPath() == null || item.getIconPath().isBlank()) {
                icon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/no_image.png")).toExternalForm()));
            } else {
                icon.setImage(new Image(CharacterAssetResolver.resolve(character.getName(), item.getIconPath())));
            }

            if (popup != null) {
                popup.getContent().clear();
                popup.getContent().add(new InventoryItemPopup(item));
            }
        }
    }
}