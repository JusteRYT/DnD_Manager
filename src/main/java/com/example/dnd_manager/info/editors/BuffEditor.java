package com.example.dnd_manager.info.editors;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.buff_debuff.BuffEditorRow;
import com.example.dnd_manager.info.buff_debuff.BuffType;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
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

    public BuffEditor(Character character) {
        super(character, "label.buffsEditor");
    }

    @Override
    protected void loadFromCharacter(Character character) {
        items.addAll(character.getBuffs());
    }

    @Override
    protected void fillInputCard(VBox inputCard) {
        nameField = new AppTextField(I18n.t("buff.promptText.name"));
        configureNameValidation(nameField);

        VBox nameBox = new VBox(2, nameField.getField(), nameRequiredLabel);
        nameBox.setMinHeight(45);

        descriptionField = new AppTextSection("", 3, I18n.t("textSection.promptText.descriptionBuffs"));

        typeBox = new AppComboBox<>();
        for (BuffType type : BuffType.values()) typeBox.getItems().add(type.getName());
        typeBox.setValue(BuffType.BUFF.getName());
        typeBox.setPrefWidth(150);

        iconPathLabel = new Label();
        iconPathLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 11px;");

        Button chooseIconButton = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        addButton = AppButtonFactory.actionSave(I18n.t("button.addBuff"));
        addButton.setPrefWidth(150);

        // ... (layout code: settingsRow, buttonsRow - оставляем как было) ...

        chooseIconButton.setOnAction(e -> {
            String path = chooseIcon();
            if (path != null) {
                iconPath.set(path);
                iconPathLabel.setText(new File(path).getName());
            }
        });

        addButton.setOnAction(e -> handleSave());

        inputCard.getChildren().addAll(
                createFieldLabel(I18n.t("textFieldLabel.name")), nameBox,
                createFieldLabel(I18n.t("textFieldLabel.description")), descriptionField,
                new HBox(15, new VBox(5, createFieldLabel(I18n.t("textFieldLabel.type")), typeBox),
                        new VBox(5, createFieldLabel(I18n.t("textFieldLabel.iconName")), iconPathLabel)),
                new HBox(15, addButton, chooseIconButton)
        );
    }

    private void handleSave() {
        if (validateName(nameField)) {
            Buff newBuff = new Buff(
                    nameField.getText().trim(),
                    descriptionField.getText(),
                    typeBox.getValue(),
                    resolveIconPath(iconPath)
            );

            if (editingItem != null) {
                int index = items.indexOf(editingItem);
                if (index != -1) items.set(index, newBuff);
                editingItem = null;
                addButton.setText(I18n.t("button.addBuff"));
            } else {
                items.add(newBuff);
            }

            refreshUI();
            clearForm();
        }
    }

    private void prepareEdit(Buff buff) {
        this.editingItem = buff;
        nameField.setText(buff.name());
        descriptionField.setText(buff.description());
        typeBox.setValue(buff.type());
        iconPath.set(buff.iconPath());
        iconPathLabel.setText(buff.iconPath().contains("/") ?
                buff.iconPath().substring(buff.iconPath().lastIndexOf("/") + 1) : "");

        addButton.setText(I18n.t("button.save"));
        nameField.getField().requestFocus();
    }

    private void clearForm() {
        nameField.clear();
        descriptionField.setText("");
        iconPath.set("");
        iconPathLabel.setText("");
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