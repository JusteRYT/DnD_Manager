package com.example.dnd_manager.info.inventory;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
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

    private final Label nameRequiredLabel = new Label(I18n.t("labelField.nameRequired"));

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

        Label title = new Label(I18n.t("label.inventoryEditor").toUpperCase());
        title.setStyle("-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 13px; -fx-letter-spacing: 1.5px;");

        if (character != null) {
            items.addAll(character.getInventory());
        }

        VBox inputCard = new VBox(12);
        inputCard.setStyle("""
                    -fx-background-color: linear-gradient(to right, #252526, #1e1e1e);
                    -fx-padding: 15;
                    -fx-background-radius: 8;
                    -fx-border-color: #3a3a3a;
                    -fx-border-radius: 8;
                """);

        AppTextField nameField = new AppTextField(I18n.t("textField.inventoryName"));
        configureNameValidation(nameField);

        VBox nameBox = new VBox(2, nameField.getField(), nameRequiredLabel);
        nameBox.setMinHeight(45);
        nameBox.setAlignment(Pos.TOP_LEFT);

        AppTextSection descriptionField = new AppTextSection("", 3, I18n.t("textSection.inventoryDescription"));
        IntegerField countField = new IntegerField(I18n.t("textField.inventoryCountPrompt"));

        AtomicReference<String> iconPath = new AtomicReference<>("");
        Label iconPathLabel = new Label();
        iconPathLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 11px;");

        Button iconButton = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        Button addButton = AppButtonFactory.actionSave(I18n.t("button.addItem"));
        addButton.setPrefWidth(150);

        HBox settingsRow = new HBox(15,
                new VBox(5, createFieldLabel(I18n.t("textFieldLabel.iconName")), iconPathLabel)
        );
        settingsRow.setAlignment(Pos.BOTTOM_LEFT);

        HBox buttonsRow = new HBox(15, addButton, iconButton);

        inputCard.getChildren().addAll(
                createFieldLabel(I18n.t("textFieldLabel.itemName")),
                nameBox,
                createFieldLabel(I18n.t("textFieldLabel.description")),
                descriptionField,
                createFieldLabel(I18n.t("textField.inventoryCount")),
                countField.getField(),
                settingsRow,
                buttonsRow
        );

        iconButton.setOnAction(e -> {
            String selected = chooseIcon();
            if (selected != null) {
                iconPath.set(selected);
                File file = new File(selected);
                iconPathLabel.setText(file.getName());
            }
        });

        addButton.setOnAction(event -> {
            if (validateName(nameField)) {
                String name = nameField.getText().trim();

                String finalIcon = iconPath.get();
                if (finalIcon == null || finalIcon.isEmpty()) {
                    finalIcon = getClass().getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm();
                }

                InventoryItem item = new InventoryItem(name, descriptionField.getText(), finalIcon, countField.getInt());
                addItem(item);

                nameField.setText("");
                descriptionField.setText("");
                iconPath.set("");
                iconPathLabel.setText("");
                nameRequiredLabel.setVisible(false);
                nameRequiredLabel.setManaged(false);
            }
        });

        listContainer.setPadding(new Insets(10, 0, 0, 0));
        getChildren().addAll(title, inputCard, listContainer);

        for (InventoryItem item : items) {
            addItemRow(item);
        }
    }

    private void configureNameValidation(AppTextField nameField) {
        nameRequiredLabel.setStyle("""
            -fx-text-fill: #ff6b6b;
            -fx-font-size: 10px;
            -fx-font-weight: bold;
            """);

        nameRequiredLabel.setVisible(false);
        nameRequiredLabel.setManaged(false);
        nameRequiredLabel.setPadding(new Insets(0, 0, 0, 5));

        nameField.getField().textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isBlank()) {
                nameRequiredLabel.setVisible(false);
                nameRequiredLabel.setManaged(false);
            }
        });
    }

    private boolean validateName(AppTextField field) {
        boolean valid = !field.getText().isBlank();
        nameRequiredLabel.setVisible(!valid);
        nameRequiredLabel.setManaged(!valid);
        return valid;
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