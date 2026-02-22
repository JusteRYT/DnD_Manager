package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.AbstractEntityEditor;
import com.example.dnd_manager.info.editors.BuffEditor;
import com.example.dnd_manager.info.editors.SkillsEditor;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.utils.SubEditorManager;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.IconStorageService;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class EditInventoryItemDialog extends BaseDialog {

    private final Character character;
    private final InventoryItem item;
    private final Consumer<InventoryItem> onItemEdited;
    private String iconPath;

    private Label buffsCountLabel;
    private Label skillsCountLabel;

    public EditInventoryItemDialog(Stage owner, Character character, InventoryItem item, Consumer<InventoryItem> onItemEdited) {
        super(owner, "Edit Item: " + item.getName(), 450, 480);
        this.character = character;
        this.item = item;
        this.onItemEdited = onItemEdited;
        this.iconPath = item.getIconPath();
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(15);

        AppTextField nameField = new AppTextField(item.getName());
        nameField.setText(item.getName());
        AppTextSection descriptionField = new AppTextSection(item.getDescription(), 4, "Description");
        IntegerField count = new IntegerField(String.valueOf(item.getCount()));

        // --- Секция баффов и скиллов ---
        VBox attachmentsBox = new VBox(10);
        attachmentsBox.setStyle("-fx-padding: 10; -fx-background-color: #252525; -fx-background-radius: 5; -fx-border-color: #3a3a3a; -fx-border-radius: 5;");

        buffsCountLabel = new Label();
        skillsCountLabel = new Label();
        updateLabels(); // Инициализация текста

        Button editBuffsBtn = AppButtonFactory.addIcon(I18n.t("buffsView.titleBuff"));
        editBuffsBtn.setOnAction(e -> openSubEditor(new BuffEditor(character), item.getAttachedBuffs(), "Edit Item Buffs"));

        Button editSkillsBtn = AppButtonFactory.addIcon(I18n.t("label.familiarsSKILLS"));
        editSkillsBtn.setOnAction(e -> openSubEditor(new SkillsEditor(character), item.getAttachedSkills(), "Edit Item Skills"));

        HBox buffRow = new HBox(15, buffsCountLabel, editBuffsBtn);
        buffRow.setAlignment(Pos.CENTER_LEFT);
        HBox skillRow = new HBox(15, skillsCountLabel, editSkillsBtn);
        skillRow.setAlignment(Pos.CENTER_LEFT);

        attachmentsBox.getChildren().addAll(new Label("Item Effects:"), buffRow, skillRow);

        // --- Кнопки сохранения ---
        Button iconBtn = AppButtonFactory.actionSave(I18n.t("editDialog.changeIcon"));
        iconBtn.setOnAction(e -> iconPath = chooseIcon());

        Button saveBtn = AppButtonFactory.actionSave(I18n.t("button.save"));
        saveBtn.setPrefWidth(120);
        saveBtn.setOnAction(e -> {
            if (nameField.getText().isBlank()) return;

            item.setName(nameField.getText());
            item.setDescription(descriptionField.getText());
            item.setCount(count.getValue());
            item.setIconPath(iconPath);

            onItemEdited.accept(item);
            close();
        });

        HBox buttonBox = new HBox(15, iconBtn, saveBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        contentArea.getChildren().addAll(
                nameField.getField(),
                descriptionField,
                count.getField(),
                attachmentsBox,
                buttonBox
        );
    }

    /**
     * Логика открытия вложенного редактора (как в InventoryEditor)
     */
    private <E> void openSubEditor(AbstractEntityEditor<E> editor, List<E> targetList, String title) {
        SubEditorManager.open(this.stage, editor, targetList, title, this::updateLabels);
    }

    private void updateLabels() {
        buffsCountLabel.setText(I18n.t("buffsView.titleBuff") + ": " + item.getAttachedBuffs().size());
        skillsCountLabel.setText(I18n.t("label.familiarsSKILLS") + ": " + item.getAttachedSkills().size());
    }

    private String chooseIcon() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Item Icon");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg"));
        File file = chooser.showOpenDialog(stage);
        if (file == null) return iconPath;
        try {
            return new IconStorageService().storeIcon(character.getName(), file);
        } catch (IOException e) {
            e.printStackTrace();
            return iconPath;
        }
    }
}