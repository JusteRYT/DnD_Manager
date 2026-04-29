package com.example.dnd_manager.info.editors.buff;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.buff_debuff.model.BuffType;
import com.example.dnd_manager.info.buff_debuff.view.BuffEditorRow;
import com.example.dnd_manager.info.editors.common.AbstractEntityEditor;
import com.example.dnd_manager.info.editors.common.EditorFormLayoutBuilder;
import com.example.dnd_manager.info.editors.common.EditorItemMutationService;
import com.example.dnd_manager.info.utils.SubEditorManager;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BuffEditor extends AbstractEntityEditor<Buff> {

    private Buff editingItem = null;
    private Button addButton;

    // Выносим поля на уровень класса для доступа из prepareEdit
    private AppTextField nameField;
    private AppTextSection descriptionField;
    private AppComboBox<String> typeBox;
    private final AtomicReference<String> iconPath = new AtomicReference<>("");
    private Label iconPathLabel;
    private final EditorItemMutationService itemMutationService = new EditorItemMutationService();
    private final BuffEditorItemFactory itemFactory = new BuffEditorItemFactory();
    private final EditorFormLayoutBuilder layoutBuilder = new EditorFormLayoutBuilder(this::createFieldLabel);
    private final BuffEditorFormBuilder formBuilder = new BuffEditorFormBuilder(layoutBuilder);

    public BuffEditor(Character character) {
        super(character, "label.buffsEditor");
        initializeEditor("label.buffsEditor");
    }

    @Override
    protected void loadFromCharacter(Character character) {
        items.addAll(character.getBuffs());
    }

    @Override
    protected void fillInputCard(VBox inputCard) {
        BuffEditorFormControls controls = formBuilder.build(inputCard, nameRequiredLabel);
        nameField = controls.nameField();
        configureNameValidation(nameField);
        descriptionField = controls.descriptionField();
        typeBox = controls.typeBox();
        iconPathLabel = controls.iconPathLabel();
        addButton = controls.saveButton();

        AppButtonFactory.attachAssetPicker(controls.assetPickerButton(), path -> {
            iconPath.set(path);
            showIconPreview(iconPathLabel, path);
        });

        controls.iconButton().setOnAction(e -> {
            String path = chooseAndImportIcon(AssetCategory.BUFFS);
            if (path != null) {
                iconPath.set(path);
                showIconPreview(iconPathLabel, path);
            }
        });

        showIconPreview(iconPathLabel, "");
        addButton.setOnAction(e -> handleSave());
    }

    private void handleSave() {
        if (validateName(nameField)) {
            Buff newBuff = itemFactory.create(
                    nameField.getText().trim(),
                    descriptionField.getText(),
                    typeBox.getValue(),
                    resolveIconPath(iconPath)
            );

            itemMutationService.addOrReplace(items, editingItem, newBuff);
            editingItem = null;
            addButton.setText(I18n.t("button.addBuff"));

            refreshUI();
            clearForm();
        }
    }

    private void prepareEdit(Buff buff) {
        openEditDialog(buff);
    }

    private void openEditDialog(Buff buff) {
        List<Buff> editBuffer = new ArrayList<>();
        editBuffer.add(buff);

        BuffEditor editor = new BuffEditor(null);
        editor.prepareEditInline(buff);

        Stage owner = getScene() != null && getScene().getWindow() instanceof Stage stage ? stage : null;
        SubEditorManager.open(owner, editor, editBuffer, I18n.t("label.buffsEditor"), () -> {
            if (!editBuffer.isEmpty()) {
                Buff updatedBuff = editBuffer.getFirst();
                itemMutationService.addOrReplace(items, buff, updatedBuff);
                refreshUI();
            }
        });
    }

    private void prepareEditInline(Buff buff) {
        this.editingItem = buff;
        nameField.setText(buff.name());
        descriptionField.setText(buff.description());
        typeBox.setValue(BuffType.displayName(buff.type()));
        iconPath.set(buff.iconPath());
        showIconPreview(iconPathLabel, buff.iconPath());

        addButton.setText(I18n.t("button.save"));
        nameField.getField().requestFocus();
    }

    private void clearForm() {
        nameField.clear();
        descriptionField.setText("");
        iconPath.set("");
        showIconPreview(iconPathLabel, "");
        nameRequiredLabel.setVisible(false);
    }

    @Override
    protected Node createItemRow(Buff buff) {
        return new BuffEditorRow(buff,
                () -> { items.remove(buff); refreshUI(); },
                () -> prepareEdit(buff),
                character);
    }

    @Override
    public void applyTo(Character character) {
        character.getBuffs().clear();
        character.getBuffs().addAll(items);
    }
}












