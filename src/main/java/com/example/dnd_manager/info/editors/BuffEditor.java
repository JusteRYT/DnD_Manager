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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class BuffEditor extends AbstractEntityEditor<Buff> {

    public BuffEditor() {
        this(null);
    }

    public BuffEditor(Character character) {
        super(character, "label.buffsEditor");
    }

    @Override
    protected void loadFromCharacter(Character character) {
        items.addAll(character.getBuffs());
    }

    @Override
    protected void fillInputCard(VBox inputCard) {
        AppTextField nameField = new AppTextField(I18n.t("buff.promptText.name"));
        configureNameValidation(nameField);

        VBox nameBox = new VBox(2, nameField.getField(), nameRequiredLabel);
        nameBox.setMinHeight(45);
        nameBox.setAlignment(Pos.TOP_LEFT);

        AppTextSection descriptionField = new AppTextSection("", 3, I18n.t("textSection.promptText.descriptionBuffs"));

        AppComboBox<String> typeBox = new AppComboBox<>();
        for (BuffType type : BuffType.values()) typeBox.getItems().add(type.getName());
        typeBox.setValue(BuffType.BUFF.getName());
        typeBox.setPrefWidth(150);

        AtomicReference<String> iconPath = new AtomicReference<>("");
        Label iconPathLabel = new Label();
        iconPathLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 11px;");

        Button chooseIconButton = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        Button addButton = AppButtonFactory.actionSave(I18n.t("button.addBuff"));
        addButton.setPrefWidth(150);

        HBox settingsRow = new HBox(15,
                new VBox(5, createFieldLabel(I18n.t("textFieldLabel.type")), typeBox),
                new VBox(5, createFieldLabel(I18n.t("textFieldLabel.iconName")), iconPathLabel)
        );
        settingsRow.setAlignment(Pos.BOTTOM_LEFT);

        HBox buttonsRow = new HBox(15, addButton, chooseIconButton);

        inputCard.getChildren().addAll(
                createFieldLabel(I18n.t("textFieldLabel.name")), nameBox,
                createFieldLabel(I18n.t("textFieldLabel.description")), descriptionField,
                settingsRow, buttonsRow
        );

        chooseIconButton.setOnAction(e -> {
            String path = chooseIcon();
            if (path != null) {
                iconPath.set(path);
                iconPathLabel.setText(new File(path).getName());
            }
        });

        addButton.setOnAction(e -> {
            if (validateName(nameField)) {
                addItem(new Buff(
                        nameField.getText().trim(),
                        descriptionField.getText(),
                        typeBox.getValue(),
                        resolveIconPath(iconPath)
                ));

                // Clear form
                nameField.clear();
                descriptionField.setText("");
                iconPath.set("");
                iconPathLabel.setText("");
                nameRequiredLabel.setVisible(false);
            }
        });
    }

    @Override
    protected Node createItemRow(Buff buff) {
        return new BuffEditorRow(buff, () -> {
            listContainer.getChildren().removeIf(node -> node instanceof BuffEditorRow r && r.getItem() == buff);
            items.remove(buff);
        }, character);
    }

    @Override
    public void applyTo(Character character) {
        character.getBuffs().clear();
        character.getBuffs().addAll(items);
    }
}