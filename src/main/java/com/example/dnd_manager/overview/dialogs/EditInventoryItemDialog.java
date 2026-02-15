package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.IconStorageService;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * Диалог редактирования предмета инвентаря.
 * Теперь наследует BaseDialog и автоматически получает CustomTitleBar.
 */
public class EditInventoryItemDialog extends BaseDialog {

    private final Character character;
    private final InventoryItem item;
    private final Consumer<InventoryItem> onItemEdited;
    private String iconPath;

    public EditInventoryItemDialog(Stage owner, Character character, InventoryItem item, Consumer<InventoryItem> onItemEdited) {
        // Передаем заголовок и размеры в базовый конструктор
        super(owner, "Edit Item: " + item.getName(), 450, 320);
        this.character = character;
        this.item = item;
        this.onItemEdited = onItemEdited;
        this.iconPath = item.getIconPath();
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(15);

        // Поля ввода
        AppTextField nameField = new AppTextField(item.getName());
        AppTextSection descriptionField = new AppTextSection(item.getDescription(), 4, "Description");
        IntegerField count = new IntegerField(String.valueOf(item.getCount()));

        // Кнопки действий
        Button iconBtn = AppButtonFactory.actionSave(I18n.t("editDialog.changeIcon"));
        iconBtn.setOnAction(e -> iconPath = chooseIcon());

        Button saveBtn = AppButtonFactory.actionSave(I18n.t("button.save"));
        saveBtn.setPrefWidth(120);
        saveBtn.setOnAction(e -> {
            if (nameField.getText().isBlank()) return;

            // Обновляем модель
            item.setName(nameField.getText());
            item.setDescription(descriptionField.getText());
            item.setCount(count.getValue());
            item.setIconPath(iconPath != null ? iconPath : "icon/no_image.png");

            onItemEdited.accept(item);
            close(); // Метод BaseDialog
        });

        HBox buttonBox = new HBox(15, iconBtn, saveBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        contentArea.getChildren().addAll(
                nameField.getField(),
                descriptionField,
                count.getField(),
                buttonBox
        );
    }

    private String chooseIcon() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Item Icon");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg"));

        File file = chooser.showOpenDialog(stage);
        if (file == null) return iconPath;

        try {
            IconStorageService storageService = new IconStorageService();
            return storageService.storeIcon(character.getName(), file);
        } catch (IOException e) {
            e.printStackTrace();
            return iconPath;
        }
    }
}