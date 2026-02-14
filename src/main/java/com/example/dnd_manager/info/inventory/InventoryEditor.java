package com.example.dnd_manager.info.inventory;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.Getter;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class InventoryEditor extends VBox {

    @Getter
    private final ObservableList<InventoryItem> items = FXCollections.observableArrayList();
    private final VBox listContainer = new VBox(8);
    private final Character character;

    public InventoryEditor() {
        this(null);
    }

    public InventoryEditor(Character character) {
        this.character = character;
        setSpacing(15);
        setPadding(new Insets(10));

        // 1. Заголовок секции (в стиле Stats/Buffs)
        Label title = new Label(I18n.t("label.inventoryEditor").toUpperCase());
        title.setStyle("-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 13px; -fx-letter-spacing: 1.5px;");

        if (character != null) {
            items.addAll(character.getInventory());
        }

        // 2. Блок ввода (Input Card) с градиентом
        VBox inputCard = new VBox(12);
        inputCard.setStyle("""
                    -fx-background-color: linear-gradient(to right, #252526, #1e1e1e);
                    -fx-padding: 15;
                    -fx-background-radius: 8;
                    -fx-border-color: #3a3a3a;
                    -fx-border-radius: 8;
                """);

        AppTextField nameField = new AppTextField(I18n.t("textField.inventoryName"));
        AppTextSection descriptionField = new AppTextSection("", 3, I18n.t("textSection.inventoryDescription"));

        AtomicReference<String> iconPath = new AtomicReference<>("");
        Label iconPathLabel = new Label();
        iconPathLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 11px;");

        Button iconButton = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        Button addButton = AppButtonFactory.actionSave(I18n.t("button.addItem"));
        addButton.setPrefWidth(150);

        HBox settingsRow = new HBox(15,
                new VBox(5, createFieldLabel("ICON_NAME"), iconPathLabel)
        );

        settingsRow.setAlignment(Pos.BOTTOM_LEFT);
        HBox buttonsRow = new HBox(15, addButton, iconButton);

        inputCard.getChildren().addAll(
                createFieldLabel("ITEM NAME"),
                nameField.getField(),
                createFieldLabel("DESCRIPTION"),
                descriptionField,
                settingsRow,
                buttonsRow
        );

        // Логика кнопок
        iconButton.setOnAction(e -> {
            String selected = chooseIcon();
            if (selected != null) {
                iconPath.set(selected);
                File file = new File(selected);
                iconPathLabel.setText(file.getName());
            }
        });

        addButton.setOnAction(event -> {
            String name = nameField.getText().trim();
            if (character == null) {
                iconPath.set(getClass().getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm());
            }
            if (!name.isEmpty()) {
                InventoryItem item = new InventoryItem(name, descriptionField.getText(), iconPath.get());
                addItem(item);

                // Очистка
                nameField.setText("");
                descriptionField.setText("");
                iconPath.set("");
                iconPathLabel.setText("");
            }
        });

        // 3. Список предметов
        listContainer.setPadding(new Insets(10, 0, 0, 0));

        getChildren().addAll(title, inputCard, listContainer);

        for (InventoryItem item : items) {
            addItemRow(item);
        }
    }

    private Label createFieldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #666; -fx-font-size: 10px; -fx-font-weight: bold;");
        return l;
    }

    private String chooseIcon() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(getScene().getWindow());
        return file != null ? file.getAbsolutePath() : null;
    }

    private void addItem(InventoryItem item) {
        items.add(item);
        addItemRow(item);
    }

    private void addItemRow(InventoryItem item) {
        InventoryRow row = new InventoryRow(item, () -> removeItem(item), character);
        listContainer.getChildren().add(row);
    }

    private void removeItem(InventoryItem item) {
        listContainer.getChildren().removeIf(node -> node instanceof InventoryRow row && row.getItem() == item);
        items.remove(item);
    }

    public void applyTo(Character character) {
        character.getInventory().clear();
        character.getInventory().addAll(items);
    }
}