package com.example.dnd_manager.info.buff_debuff;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextField;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class BuffEditor extends VBox {

    private final ObservableList<Buff> buffs = FXCollections.observableArrayList();
    private final VBox listContainer = new VBox(8);
    private final Character character;

    public BuffEditor() {
        this(null);
    }

    /**
     * Create editor with initial buffs (for EDIT mode) or empty (CREATE mode).
     */
    public BuffEditor(Character character) {
        this.character = character;
        setSpacing(15);
        setPadding(new Insets(10));

        // 1. Заголовок секции
        Label title = new Label(I18n.t("label.buffsEditor").toUpperCase());
        title.setStyle("-fx-text-fill: #c89b3c; -fx-font-weight: bold; -fx-font-size: 13px; -fx-letter-spacing: 1.5px;");

        if (character != null) {
            buffs.addAll(character.getBuffs());
        }

        // 2. Блок ввода (Input Card)
        VBox inputCard = new VBox(12);
        inputCard.setStyle("""
                    -fx-background-color: linear-gradient(to right, #252526, #1e1e1e);
                    -fx-padding: 15;
                    -fx-background-radius: 8;
                    -fx-border-color: #3a3a3a;
                    -fx-border-radius: 8;
                """);

        AppTextField nameField = new AppTextField(I18n.t("buff.promptText.name"));
        AppTextSection descriptionField = new AppTextSection("", 3, I18n.t("textSection.promptText.descriptionBuffs"));

        AppComboBox<BuffType> typeBox = new AppComboBox<>();
        typeBox.getItems().addAll(BuffType.values());
        typeBox.setValue(BuffType.BUFF);
        typeBox.setPrefWidth(150);

        AtomicReference<String> iconPath = new AtomicReference<>("");
        Label iconPathLabel = new Label();
        iconPathLabel.setStyle("-fx-text-fill: #FFC107; -fx-font-size: 11px;");

        Button chooseIconButton = AppButtonFactory.addIcon(I18n.t("button.addIcon"));
        Button addButton = AppButtonFactory.actionSave(I18n.t("button.addBuff"));
        addButton.setPrefWidth(150);

        // Вспомогательная строка для типа и кнопки иконки
        HBox settingsRow = new HBox(15,
                new VBox(5, createFieldLabel("TYPE"), typeBox),
                new VBox(5, createFieldLabel("ICON_NAME"), iconPathLabel)
        );
        settingsRow.setAlignment(Pos.BOTTOM_LEFT);

        HBox buttonsRow = new HBox(15, addButton, chooseIconButton);

        inputCard.getChildren().addAll(
                createFieldLabel("NAME"),
                nameField.getField(),
                createFieldLabel("DESCRIPTION"),
                descriptionField,
                settingsRow,
                buttonsRow
        );

        // --- ЛОГИКА КНОПОК ---
        chooseIconButton.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
            File file = chooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                iconPath.set(file.getAbsolutePath());
                iconPathLabel.setText(file.getName());
            }
        });

        addButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (character == null) {
                iconPath.set(getClass().getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm());
            }
            if (!name.isEmpty()) {
                Buff buff = new Buff(name, descriptionField.getText(), typeBox.getValue(), iconPath.get());
                addBuff(buff);

                // Очистка
                nameField.clear();
                descriptionField.setText("");
                iconPath.set("");
                iconPathLabel.setText("");
            }
        });

        // 3. Список добавленных баффов
        listContainer.setPadding(new Insets(10, 0, 0, 0));

        getChildren().addAll(title, inputCard, listContainer);

        // Отрисовка существующих
        for (Buff buff : buffs) {
            addBuffRow(buff);
        }
    }

    // Вспомогательный метод для маленьких подписей
    private Label createFieldLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #666; -fx-font-size: 10px; -fx-font-weight: bold;");
        return l;
    }

    private void addBuff(Buff buff) {
        buffs.add(buff);
        addBuffRow(buff);
    }

    private void addBuffRow(Buff buff) {
        BuffEditorRow row = new BuffEditorRow(buff, () -> removeBuff(buff), character);
        listContainer.getChildren().add(row);
    }

    private void removeBuff(Buff buff) {
        listContainer.getChildren().removeIf(node -> node instanceof BuffEditorRow r && r.getBuff() == buff);
        buffs.remove(buff);
    }

    public void applyTo(Character character) {
        character.getBuffs().clear();
        character.getBuffs().addAll(buffs);
    }

    public ObservableList<Buff> getBuffs() {
        return FXCollections.unmodifiableObservableList(buffs);
    }
}