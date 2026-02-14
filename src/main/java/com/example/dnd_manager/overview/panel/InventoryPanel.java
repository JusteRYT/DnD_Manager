package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.inventory.InventoryItemPopup;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.AddInventoryItemDialog;
import com.example.dnd_manager.overview.dialogs.EditInventoryItemDialog;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * Inventory panel with square item cells like a grid.
 */
public class InventoryPanel extends VBox {

    private static final int ITEM_SIZE = 60;
    private static final int ITEM_CELL_SIZE = 70;

    private final Character character;
    private final FlowPane itemsPane;
    private final Consumer<Character> onCharacterUpdated;

    private boolean deleteMode = false;
    private final Button deleteBtn;

    public InventoryPanel(Character character, Consumer<Character> onCharacterUpdated) {
        this.character = character;
        this.onCharacterUpdated = onCharacterUpdated;
        setSpacing(6);


        Label title = new Label(I18n.t("inventoryPanel.title"));
        title.setStyle("-fx-text-fill: #c89b3c; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button addBtn = AppButtonFactory.customButton("+", 24);
        addBtn.setOnAction(e -> new AddInventoryItemDialog(character, this::onItemCreated).show());

        deleteBtn = AppButtonFactory.deleteToggleButton("-", 30);
        deleteBtn.setOnMouseClicked(e -> deleteMode = deleteBtn.getStyle().contains(AppTheme.BUTTON_DANGER));

        HBox header = new HBox(8, title, addBtn, deleteBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox.setMargin(title, new Insets(0, 0, 4, 0));
        itemsPane = new FlowPane(5, 5);
        itemsPane.setPadding(new Insets(4));
        itemsPane.setPrefWrapLength(ITEM_CELL_SIZE * 6);

        character.getInventory().forEach(this::addItem);

        setStyle("""
                -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1f1f1f);
                -fx-background-radius: 10;
                -fx-border-color: #c89b3c;
                -fx-border-radius: 10;
                -fx-border-width: 1;
                -fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.15), 15, 0, 0, 0);
                """);
        setPadding(new Insets(8));
        getChildren().addAll(header, itemsPane);
    }

    private void toggleDeleteMode() {
        deleteMode = !deleteMode;
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
        if (deleteMode) {
            toggleDeleteMode();
        }
    }

    private void editItem(InventoryItem item, InventoryItemCell view) {
        new EditInventoryItemDialog(character, item, updated -> {
            view.refresh();
            onCharacterUpdated.accept(character);
        }).show();
    }

    private class InventoryItemCell extends StackPane {

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
            container.setOnMouseEntered(e -> container.setStyle(container.getStyle() + "-fx-border-color: #c89b3c; -fx-effect: dropshadow(two-pass-box, rgba(200, 155, 60, 0.3), 5, 0, 0, 0);"));
            container.setOnMouseExited(e -> container.setStyle(container.getStyle().replace("-fx-border-color: #c89b3c;", "-fx-border-color: #3a3a3a;").split("-fx-effect")[0]));
            container.setAlignment(Pos.CENTER);

            getChildren().add(container);
            refresh();

            // ===== POPUP =====
            popup = new Popup();
            popup.setAutoHide(true);
            popup.setAutoFix(true);
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

            // ===== Клик по иконке =====
            icon.setOnMouseClicked(e -> {
                if (deleteMode) {
                    onRemove.accept(item, this);
                } else {

                }
            });
        }

        public void refresh() {
            if (item.getIconPath() == null || item.getIconPath().isBlank()) {
                icon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/images.png")).toExternalForm()));
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