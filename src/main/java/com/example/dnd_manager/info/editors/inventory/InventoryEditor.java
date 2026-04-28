package com.example.dnd_manager.info.editors.inventory;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.editors.buff.BuffEditor;
import com.example.dnd_manager.info.editors.common.AbstractEntityEditor;
import com.example.dnd_manager.info.editors.common.EditorFormLayoutBuilder;
import com.example.dnd_manager.info.editors.common.EditorItemMutationService;
import com.example.dnd_manager.info.editors.common.IconPathDisplayFormatter;
import com.example.dnd_manager.info.editors.skills.SkillsEditor;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.info.inventory.view.InventoryRow;
import com.example.dnd_manager.info.skills.model.Skill;
import com.example.dnd_manager.info.utils.SubEditorManager;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class InventoryEditor extends AbstractEntityEditor<InventoryItem> {

    // Поля теперь доступны всему классу для логики редактирования
    private InventoryItem editingItem = null;
    private Button addButton;

    private AppTextField nameField;
    private AppTextSection descriptionField;
    private IntegerField countField;
    private final AtomicReference<String> iconPath = new AtomicReference<>("");
    private Label iconPathLabel;
    private final List<Buff> tempBuffs = new ArrayList<>();
    private final List<Skill> tempSkills = new ArrayList<>();
    private Label effectsInfoLabel;
    private final InventoryEditorEffectsSummaryFormatter effectsSummaryFormatter = new InventoryEditorEffectsSummaryFormatter();
    private final EditorItemMutationService itemMutationService = new EditorItemMutationService();
    private final IconPathDisplayFormatter iconPathDisplayFormatter = new IconPathDisplayFormatter();
    private final InventoryEditorItemFactory itemFactory = new InventoryEditorItemFactory();
    private final EditorFormLayoutBuilder layoutBuilder = new EditorFormLayoutBuilder(this::createFieldLabel);
    private final InventoryEditorFormBuilder formBuilder = new InventoryEditorFormBuilder(
            layoutBuilder,
            effectsSummaryFormatter
    );

    public InventoryEditor(Character character) {
        super(character, "label.inventoryEditor");
    }

    @Override
    protected void loadFromCharacter(Character character) {
        items.addAll(character.getInventory());
    }

    @Override
    protected void fillInputCard(VBox inputCard) {
        InventoryEditorFormControls controls = formBuilder.build(inputCard, nameRequiredLabel);
        nameField = controls.nameField();
        configureNameValidation(nameField);
        descriptionField = controls.descriptionField();
        countField = controls.countField();
        iconPathLabel = controls.iconPathLabel();
        effectsInfoLabel = controls.effectsInfoLabel();
        addButton = controls.saveButton();

        controls.addBuffButton().setOnAction(e ->
                openSubEditor(new BuffEditor(character), tempBuffs, I18n.t("dialog.inventory.buffs.editEditorTitle")));
        controls.addSkillButton().setOnAction(e ->
                openSubEditor(new SkillsEditor(character), tempSkills, I18n.t("dialog.inventory.skills.editEditorTitle")));

        AppButtonFactory.attachAssetPicker(controls.assetPickerButton(), path -> {
            iconPath.set(path);
            iconPathLabel.setText(iconPathDisplayFormatter.fileNameOrEmpty(path));
        });

        controls.iconButton().setOnAction(e -> {
            String path = chooseAndImportIcon(AssetCategory.ITEMS);
            if (path != null) {
                iconPath.set(path);
                iconPathLabel.setText(iconPathDisplayFormatter.fileNameOrEmpty(path));
            }
        });

        addButton.setOnAction(event -> handleSave());
    }

    private <E> void openSubEditor(AbstractEntityEditor<E> editor, List<E> targetList, String title) {
        // Здесь owner можно получить через getScene().getWindow()
        Stage owner = (Stage) this.getScene().getWindow();
        SubEditorManager.open(owner, editor, targetList, title, this::updateEffectsLabel);
    }

    private void updateEffectsLabel() {
        effectsInfoLabel.setText(effectsSummaryFormatter.format(tempBuffs.size(), tempSkills.size()));
    }

    private void handleSave() {
        if (validateName(nameField)) {
            InventoryItem newItem = itemFactory.create(
                    nameField.getText().trim(),
                    descriptionField.getText(),
                    resolveIconPath(iconPath),
                    countField.getInt(),
                    tempBuffs,
                    tempSkills
            );

            itemMutationService.addOrReplace(items, editingItem, newItem);
            editingItem = null;
            addButton.setText(I18n.t("button.addItem"));

            refreshUI();
            clearForm();
        }
    }

    private void prepareEdit(InventoryItem item) {
        this.editingItem = item;
        nameField.setText(item.getName());
        descriptionField.setText(item.getDescription());
        countField.getField().setText(String.valueOf(item.getCount()));
        iconPath.set(item.getIconPath());

        tempBuffs.clear();
        tempBuffs.addAll(item.getAttachedBuffs());
        tempSkills.clear();
        tempSkills.addAll(item.getAttachedSkills());
        updateEffectsLabel();

        iconPathLabel.setText(iconPathDisplayFormatter.fileNameOrFallback(
                item.getIconPath(),
                I18n.t("inventoryEditor.icon.noneSelected")
        ));

        addButton.setText(I18n.t("button.save"));
        nameField.getField().requestFocus();
    }

    private void clearForm() {
        nameField.clear();
        descriptionField.setText("");
        countField.getField().setText("");
        iconPath.set("");
        iconPathLabel.setText("");
        tempBuffs.clear();
        tempSkills.clear();
        updateEffectsLabel();
        nameRequiredLabel.setVisible(false);
    }

    @Override
    protected Node createItemRow(InventoryItem item) {
        return new InventoryRow(
                item,
                () -> {
                    items.remove(item);
                    refreshUI();
                },
                () -> prepareEdit(item),
                () -> {
                    openSubEditor(new BuffEditor(character), item.getAttachedBuffs(), I18n.t("dialog.inventory.buffs.editEditorTitle") + ": " + item.getName());
                    refreshUI();
                },
                () -> {
                    openSubEditor(new SkillsEditor(character), item.getAttachedSkills(), I18n.t("dialog.inventory.skills.editEditorTitle") + ": " + item.getName());
                    refreshUI();
                },
                character
        );
    }

    @Override
    public void applyTo(Character character) {
        character.getInventory().clear();
        character.getInventory().addAll(items);
    }
}












