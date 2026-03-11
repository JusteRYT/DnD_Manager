package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.editors.AbstractEntityEditor;
import com.example.dnd_manager.info.editors.BuffEditor;
import com.example.dnd_manager.info.editors.SkillsEditor;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.utils.SubEditorManager;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.IconStorageService;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddInventoryItemDialog extends BaseDialog {

    private final Character character;
    private final Consumer<InventoryItem> onItemAddedOrEdited;
    private final InventoryItem existingItem;
    private String iconPath;
    private final List<Buff> attachedBuffs = new ArrayList<>();
    private final List<Skill> attachedSkills = new ArrayList<>();
    private Label buffsCountLabel;
    private Label skillsCountLabel;

    public AddInventoryItemDialog(Stage owner, Character character, InventoryItem itemToEdit, Consumer<InventoryItem> onComplete) {
        super(owner,
                itemToEdit == null ? "Add Inventory Item" : "Edit Inventory Item",
                450, 500);

        this.character = character;
        this.existingItem = itemToEdit;
        this.onItemAddedOrEdited = onComplete;

        if (existingItem != null) {
            this.iconPath = existingItem.getIconPath();
        }
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(15);

        AppTextField nameField = new AppTextField(
                existingItem != null ? existingItem.getName() : "Item name", true
        );
        AppTextSection descriptionField = new AppTextSection(
                existingItem != null ? existingItem.getDescription() : "", 3, "Description"
        );
        IntegerField countField = new IntegerField(
                existingItem != null ? String.valueOf(existingItem.getCount()) : "1", true
        );

        Button iconBtn = AppButtonFactory.addIcon(I18n.t("buttonText.icon"));
        iconBtn.setOnAction(e -> iconPath = chooseIcon());

        Button saveBtn = AppButtonFactory.actionSave(existingItem == null ? I18n.t("button.addItem") : I18n.t("button.save"));
        saveBtn.setOnAction(e -> {
            if (nameField.getText().isBlank()) return;
            saveData(nameField.getText(), descriptionField.getText(), countField.getText());
            close();
        });

        Button chooseAssets = AppButtonFactory.assetPickerButton();
        AppButtonFactory.attachAssetPicker(chooseAssets, path -> iconPath = path);

        HBox actions = new HBox(10, saveBtn, iconBtn, chooseAssets);

        VBox attachmentsBox = new VBox(10);
        attachmentsBox.setStyle("-fx-padding: 10; -fx-background-color: #252525; -fx-background-radius: 5; -fx-border-color: #3a3a3a; -fx-border-radius: 5;");

        Label sectionLabel = new Label("Item Attachments");
        sectionLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px; -fx-font-weight: bold;");

        buffsCountLabel = new Label();
        skillsCountLabel = new Label();
        updateLabels();

        Button editBuffsBtn = AppButtonFactory.addIcon("Buffs");
        editBuffsBtn.setOnAction(e ->
                openSubEditor(new BuffEditor(character), attachedBuffs, "Item Buffs")
        );

        Button editSkillsBtn = AppButtonFactory.addIcon("Skills");
        editSkillsBtn.setOnAction(e ->
                openSubEditor(new SkillsEditor(character), attachedSkills, "Item Skills")
        );

        HBox buffRow = new HBox(15, buffsCountLabel, editBuffsBtn);
        HBox skillRow = new HBox(15, skillsCountLabel, editSkillsBtn);

        attachmentsBox.getChildren().addAll(sectionLabel, buffRow, skillRow);


        contentArea.getChildren().addAll(
                nameField.getField(),
                descriptionField,
                countField.getField(),
                attachmentsBox,
                actions
        );

    }

    private void saveData(String name, String desc, String countStr) {
        int count = 1;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException ignored) {
        }

        if (existingItem != null) {
            existingItem.setName(name);
            existingItem.setDescription(desc);
            existingItem.setCount(count);
            existingItem.setIconPath(iconPath != null ? iconPath : "icon/no_image.png");
            existingItem.setAttachedBuffs(new ArrayList<>(attachedBuffs));
            existingItem.setAttachedSkills(new ArrayList<>(attachedSkills));
            onItemAddedOrEdited.accept(existingItem);
        } else {
            InventoryItem item = new InventoryItem(
                    name,
                    desc,
                    iconPath != null && !iconPath.equals("icon/no_image.png") ? iconPath : "icon/no_image.png"
            );
            item.setCount(count);
            item.setAttachedBuffs(new ArrayList<>(attachedBuffs));
            item.setAttachedSkills(new ArrayList<>(attachedSkills));
            character.getInventory().add(item);
            onItemAddedOrEdited.accept(item);
        }
    }

    private String chooseIcon() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg"));
        File file = chooser.showOpenDialog(stage);
        if (file == null) return null;

        try {
            return new IconStorageService().storeIcon(character.getName(), file);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Updates UI counters for buffs and skills.
     */
    private void updateLabels() {
        buffsCountLabel.setText("Buffs: " + attachedBuffs.size());
        skillsCountLabel.setText("Skills: " + attachedSkills.size());
    }

    /**
     * Opens nested editor for editing item attachments.
     */
    private <E> void openSubEditor(AbstractEntityEditor<E> editor,
                                   List<E> targetList,
                                   String title) {

        SubEditorManager.open(this.stage, editor, targetList, title, this::updateLabels);
    }

}