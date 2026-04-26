package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.assets.service.GlobalAssetService;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.editors.AbstractEntityEditor;
import com.example.dnd_manager.info.editors.BuffEditor;
import com.example.dnd_manager.info.editors.SkillsEditor;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;
import com.example.dnd_manager.info.utils.SubEditorManager;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppCheckBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class AddInventoryItemDialog extends BaseDialog {

    private static final String DEFAULT_ICON_PATH = "icon/no_image.png";

    private final Character character;
    private final Consumer<InventoryItem> onItemAddedOrEdited;
    private final InventoryItemIconChooser iconChooser;
    private final InventoryItemMutationService mutationService;
    private final InventoryItem existingItem;
    private String iconPath;
    private final List<Buff> attachedBuffs = new ArrayList<>();
    private final List<Skill> attachedSkills = new ArrayList<>();
    private Label buffsCountLabel;
    private Label skillsCountLabel;
    private AppCheckBox equippedCheckBox;
    private AppTextField effectDisplayField;

    public AddInventoryItemDialog(Stage owner, Character character, InventoryItem itemToEdit, Consumer<InventoryItem> onComplete) {
        this(owner, character, itemToEdit, onComplete, new GlobalAssetService(), new InventoryItemMutationService());
    }

    public AddInventoryItemDialog(
            Stage owner,
            Character character,
            InventoryItem itemToEdit,
            Consumer<InventoryItem> onComplete,
            GlobalAssetService globalAssetService
    ) {
        this(owner, character, itemToEdit, onComplete, globalAssetService, new InventoryItemMutationService());
    }

    AddInventoryItemDialog(
            Stage owner,
            Character character,
            InventoryItem itemToEdit,
            Consumer<InventoryItem> onComplete,
            GlobalAssetService globalAssetService,
            InventoryItemMutationService mutationService
    ) {
        super(owner,
                itemToEdit == null ? I18n.t("dialog.inventory.add.title") : I18n.t("dialog.inventory.edit.title"),
                450, 550);

        this.character = character;
        this.existingItem = itemToEdit;
        this.onItemAddedOrEdited = onComplete;
        this.iconChooser = new InventoryItemIconChooser(Objects.requireNonNull(globalAssetService));
        this.mutationService = Objects.requireNonNull(mutationService, "mutationService must not be null");

        if (existingItem != null) {
            this.iconPath = existingItem.getIconPath();
            this.attachedBuffs.addAll(existingItem.getAttachedBuffs());
            this.attachedSkills.addAll(existingItem.getAttachedSkills());
        }
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(0); // Убираем дефолтный спейсинг

        // 1. Создаем контейнер для полей, которые будут скроллиться
        VBox scrollContent = new VBox(15);
        scrollContent.setPadding(new Insets(0, 15, 10, 0));

        AppTextField nameField = new AppTextField(
                existingItem != null ? existingItem.getName() : I18n.t("textField.inventoryName"), true
        );
        AppTextSection descriptionField = new AppTextSection(
                existingItem != null ? existingItem.getDescription() : "", 3, I18n.t("dialog.inventory.description.label")
        );
        IntegerField countField = new IntegerField(
                existingItem != null ? String.valueOf(existingItem.getCount()) : "1", true
        );

        // --- Эффекты и чекбокс ---
        equippedCheckBox = new AppCheckBox(I18n.t("dialog.inventory.equipped"));
        equippedCheckBox.setSelected(existingItem != null && existingItem.isEquipped());

        effectDisplayField = new AppTextField(
                (existingItem != null && existingItem.getCustomEffectName() != null) ?
                        existingItem.getCustomEffectName() : I18n.t("textField.inventoryName"), false
        );
        effectDisplayField.getField().setPromptText(I18n.t("dialog.inventory.effectDisplay.prompt"));

        VBox effectFieldContainer = new VBox(
                5,
                new Label(I18n.t("dialog.inventory.effectDisplay.label")),
                effectDisplayField.getField()
        );
        effectFieldContainer.setVisible(equippedCheckBox.isSelected());

        equippedCheckBox.setOnAction(() -> effectFieldContainer.setVisible(equippedCheckBox.isSelected()));

        // --- Секция баффов и скиллов ---
        VBox attachmentsBox = new VBox(10);
        attachmentsBox.setStyle("-fx-padding: 10; -fx-background-color: #252525; -fx-background-radius: 5; -fx-border-color: #3a3a3a; -fx-border-radius: 5;");

        Label sectionLabel = new Label(I18n.t("dialog.inventory.attachments.title"));
        sectionLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 14px; -fx-font-weight: bold;");

        buffsCountLabel = new Label();
        skillsCountLabel = new Label();
        updateLabels();

        Button editBuffsBtn = AppButtonFactory.addIcon(I18n.t("dialog.inventory.buffs.short"));
        editBuffsBtn.setOnAction(e -> openSubEditor(new BuffEditor(character), attachedBuffs, I18n.t("dialog.inventory.buffs.editorTitle")));

        Button editSkillsBtn = AppButtonFactory.addIcon(I18n.t("dialog.inventory.skills.short"));
        editSkillsBtn.setOnAction(e -> openSubEditor(new SkillsEditor(character), attachedSkills, I18n.t("dialog.inventory.skills.editorTitle")));

        HBox buffRow = new HBox(15, buffsCountLabel, editBuffsBtn);
        buffRow.setAlignment(Pos.CENTER_LEFT);
        HBox skillRow = new HBox(15, skillsCountLabel, editSkillsBtn);
        skillRow.setAlignment(Pos.CENTER_LEFT);

        attachmentsBox.getChildren().addAll(sectionLabel, buffRow, skillRow);

        // Собираем все прокручиваемые элементы
        scrollContent.getChildren().addAll(
                nameField.getField(),
                descriptionField,
                countField.getField(),
                equippedCheckBox,
                effectFieldContainer,
                attachmentsBox
        );

        // 2. Оборачиваем в ScrollPane
        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(scrollContent);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // 3. Фиксированная панель кнопок
        Button iconBtn = AppButtonFactory.addIcon(I18n.t("buttonText.icon"));
        iconBtn.setOnAction(e -> iconPath = iconChooser.chooseItemIcon(stage, iconPath));

        Button saveBtn = AppButtonFactory.actionSave(existingItem == null ? I18n.t("button.addItem") : I18n.t("button.save"));
        saveBtn.setOnAction(e -> {
            if (nameField.getText().isBlank()) return;
            saveData(nameField.getText(), descriptionField.getText(), countField.getText());
            close();
        });

        Button chooseAssets = AppButtonFactory.assetPickerButton();
        AppButtonFactory.attachAssetPicker(chooseAssets, path -> iconPath = path);

        HBox actions = new HBox(10, iconBtn, chooseAssets, saveBtn);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPadding(new Insets(15, 0, 0, 0)); // Отступ сверху от скролла

        // 4. Компоновка
        contentArea.getChildren().addAll(scrollPane, actions);
    }

    private void saveData(String name, String desc, String countStr) {
        int count = 1;
        try {
            count = Integer.parseInt(countStr);
        } catch (NumberFormatException ignored) {
        }

        if (existingItem != null) {
            mutationService.applyToExisting(
                    existingItem,
                    name,
                    desc,
                    count,
                    iconPath,
                    DEFAULT_ICON_PATH,
                    equippedCheckBox.isSelected(),
                    effectDisplayField.getText(),
                    attachedBuffs,
                    attachedSkills
            );
            onItemAddedOrEdited.accept(existingItem);
        } else {
            InventoryItem item = mutationService.createNew(
                    name,
                    desc,
                    count,
                    iconPath,
                    DEFAULT_ICON_PATH,
                    equippedCheckBox.isSelected(),
                    effectDisplayField.getText(),
                    attachedBuffs,
                    attachedSkills
            );
            character.getInventory().add(item);
            onItemAddedOrEdited.accept(item);
        }
    }

    /**
     * Updates UI counters for buffs and skills.
     */
    private void updateLabels() {
        buffsCountLabel.setText(I18n.t("textLabel.buffsItemInventory") + " " + attachedBuffs.size());
        skillsCountLabel.setText(I18n.t("dialog.inventory.skills.count") + " " + attachedSkills.size());
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
