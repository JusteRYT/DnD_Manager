package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.AbstractEntityEditor;
import com.example.dnd_manager.info.editors.BuffEditor;
import com.example.dnd_manager.info.editors.SkillsEditor;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.utils.SubEditorManager;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.IconStorageService;
import com.example.dnd_manager.theme.AppCheckBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class EditInventoryItemDialog extends BaseDialog {

    private final Character character;
    private final InventoryItem item;
    private final Consumer<InventoryItem> onItemEdited;
    private String iconPath;

    private Label buffsCountLabel;
    private Label skillsCountLabel;
    private AppCheckBox equippedCheckBox;
    private AppTextField effectDisplayField;

    public EditInventoryItemDialog(Stage owner, Character character, InventoryItem item, Consumer<InventoryItem> onItemEdited) {
        super(owner, I18n.t("title.editDialog") + item.getName(), 450, 550);
        this.character = character;
        this.item = item;
        this.onItemEdited = onItemEdited;
        this.iconPath = item.getIconPath();
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(0);

        VBox scrollContent = new VBox(15);
        scrollContent.setPadding(new Insets(0, 15, 10, 0));

        AppTextField nameField = new AppTextField(item.getName(), true);
        nameField.setText(item.getName());
        AppTextSection descriptionField = new AppTextSection(item.getDescription(), 4, I18n.t("label.familiarsDescription"));
        IntegerField count = new IntegerField(String.valueOf(item.getCount()), false);

        // --- Секция баффов и скиллов ---
        VBox attachmentsBox = new VBox(10);
        attachmentsBox.setStyle("-fx-padding: 10; -fx-background-color: #252525; -fx-background-radius: 5; -fx-border-color: #3a3a3a; -fx-border-radius: 5;");

        Label itemSectionLabel = new Label(I18n.t("label.editDialog"));
        itemSectionLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px; -fx-font-weight: bold;");

        buffsCountLabel = new Label();
        skillsCountLabel = new Label();
        updateLabels();

        Button editBuffsBtn = AppButtonFactory.addIcon(I18n.t("buffsView.titleBuff"));
        editBuffsBtn.setOnAction(e -> openSubEditor(new BuffEditor(character), item.getAttachedBuffs(), "Edit Item Buffs"));

        Button editSkillsBtn = AppButtonFactory.addIcon(I18n.t("label.familiarsSKILLS"));
        editSkillsBtn.setOnAction(e -> openSubEditor(new SkillsEditor(character), item.getAttachedSkills(), "Edit Item Skills"));

        HBox buffRow = new HBox(15, buffsCountLabel, editBuffsBtn);
        buffRow.setAlignment(Pos.CENTER_LEFT);
        HBox skillRow = new HBox(15, skillsCountLabel, editSkillsBtn);
        skillRow.setAlignment(Pos.CENTER_LEFT);

        attachmentsBox.getChildren().addAll(itemSectionLabel, buffRow, skillRow);

        effectDisplayField = new AppTextField(item.getCustomEffectName() != null ?
                item.getCustomEffectName() : item.getName(), false);
        effectDisplayField.getField().setPromptText("Display name in TopBar (e.g. +1 AC)");

        VBox effectFieldContainer = new VBox(5, new Label("Effect Display Name:"), effectDisplayField.getField());
        effectFieldContainer.setVisible(item.isEquipped());

        equippedCheckBox = new AppCheckBox("Equip this item");
        equippedCheckBox.setSelected(item.isEquipped());
        equippedCheckBox.setOnAction(() -> {
            effectFieldContainer.setVisible(equippedCheckBox.isSelected());
        });

        scrollContent.getChildren().addAll(
                nameField.getField(),
                descriptionField,
                count.getField(),
                equippedCheckBox,
                effectFieldContainer,
                attachmentsBox
        );

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(scrollContent);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // --- Кнопки сохранения ---
        Button iconBtn = AppButtonFactory.actionSave(I18n.t("editDialog.changeIcon"));
        iconBtn.setOnAction(e -> iconPath = chooseIcon());

        Button chooseButton = AppButtonFactory.assetPickerButton();
        AppButtonFactory.attachAssetPicker(chooseButton, path -> iconPath = path);

        Button saveBtn = AppButtonFactory.actionSave(I18n.t("button.save"));
        saveBtn.setPrefWidth(120);
        saveBtn.setOnAction(e -> {
            if (nameField.getText().isBlank()) return;

            item.setName(nameField.getText());
            item.setDescription(descriptionField.getText());
            item.setCount(count.getValue());
            item.setIconPath(iconPath);
            item.setEquipped(equippedCheckBox.isSelected());
            item.setCustomEffectName(effectDisplayField.getText());

            onItemEdited.accept(item);
            close();
        });

        HBox buttonBox = new HBox(15, iconBtn, saveBtn, chooseButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        contentArea.getChildren().addAll(scrollPane, buttonBox);
    }

    /**
     * Логика открытия вложенного редактора (как в InventoryEditor)
     */
    private <E> void openSubEditor(AbstractEntityEditor<E> editor, List<E> targetList, String title) {
        SubEditorManager.open(this.stage, editor, targetList, title, this::updateLabels);
    }

    private void updateLabels() {
        buffsCountLabel.setText(I18n.t("buffsView.titleBuff") + ": " + item.getAttachedBuffs().size());
        buffsCountLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11px; -fx-font-style: italic;");
        skillsCountLabel.setText(I18n.t("label.familiarsSKILLS") + ": " + item.getAttachedSkills().size());
        skillsCountLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11px; -fx-font-style: italic;");
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
            log.error(e.getMessage());
            return iconPath;
        }
    }
}