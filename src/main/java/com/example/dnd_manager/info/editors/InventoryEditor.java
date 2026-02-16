package com.example.dnd_manager.info.editors;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.inventory.InventoryRow;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class InventoryEditor extends AbstractEntityEditor<InventoryItem> {

    public InventoryEditor() {
        this(null);
    }

    public InventoryEditor(Character character) {
        super(character, "label.inventoryEditor");
    }

    @Override
    protected void loadFromCharacter(Character character) {
        items.addAll(character.getInventory());
    }

    @Override
    protected void fillInputCard(VBox inputCard) {
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
                createFieldLabel(I18n.t("textFieldLabel.itemName")), nameBox,
                createFieldLabel(I18n.t("textFieldLabel.description")), descriptionField,
                createFieldLabel(I18n.t("textField.inventoryCount")), countField.getField(),
                settingsRow, buttonsRow
        );

        iconButton.setOnAction(e -> {
            String path = chooseIcon();
            if (path != null) {
                iconPath.set(path);
                iconPathLabel.setText(new File(path).getName());
            }
        });

        addButton.setOnAction(event -> {
            if (validateName(nameField)) {
                addItem(new InventoryItem(
                        nameField.getText().trim(),
                        descriptionField.getText(),
                        resolveIconPath(iconPath),
                        countField.getInt()
                ));

                // Clear form
                nameField.clear();
                descriptionField.setText("");
                countField.getField().setText("");
                iconPath.set("");
                iconPathLabel.setText("");
                nameRequiredLabel.setVisible(false);
            }
        });
    }

    @Override
    protected Node createItemRow(InventoryItem item) {
        return new InventoryRow(item, () -> {
            itemsContainer.getChildren().removeIf(node -> node instanceof InventoryRow row && row.getItem() == item);
            items.remove(item);
        }, character);
    }

    @Override
    public void applyTo(Character character) {
        character.getInventory().clear();
        character.getInventory().addAll(items);
    }
}