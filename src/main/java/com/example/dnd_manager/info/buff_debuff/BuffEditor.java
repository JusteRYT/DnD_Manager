package com.example.dnd_manager.info.buff_debuff;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppButtonFactory;
import com.example.dnd_manager.theme.AppComboBox;
import com.example.dnd_manager.theme.AppTextSection;
import com.example.dnd_manager.theme.AppTheme;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Editor component for character buffs and debuffs.
 * Supports CREATE and EDIT modes without сразу изменять Character.
 */
public class BuffEditor extends VBox {

    private final ObservableList<Buff> buffs = FXCollections.observableArrayList();
    private final VBox listContainer = new VBox(5);
    private final Character character;

    public BuffEditor() {
        this(null);
    }

    /**
     * Create editor with initial buffs (for EDIT mode) or empty (CREATE mode).
     */
    public BuffEditor(Character character) {
        setSpacing(10);
        this.character = character;

        Label title = new Label(I18n.t("label.buffsEditor"));
        title.setStyle("""
                -fx-text-fill: %s;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
            """.formatted(AppTheme.TEXT_ACCENT));

        if (character != null) {
            buffs.addAll(character.getBuffs());
        }

        // UI элементы
        TextField nameField = createStyledTextField();
        AppTextSection descriptionField = new AppTextSection("", 4, I18n.t("textSection.promptText.descriptionBuffs"));
        AppComboBox<BuffType> typeBox = new AppComboBox<>();
        typeBox.getItems().addAll(BuffType.values());
        typeBox.setValue(BuffType.BUFF);

        AtomicReference<String> iconPath = new AtomicReference<>("");

        Label notificationLabel = new Label();
        notificationLabel.setStyle("""
                -fx-text-fill: %s;
                -fx-font-size: 12px;
            """.formatted(AppTheme.BUTTON_PRIMARY));
        notificationLabel.setVisible(false);

        // Кнопка выбора иконки
        Button chooseIconButton = AppButtonFactory.primary(I18n.t("buttonText.icon"));
        chooseIconButton.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle(I18n.t("chooser.labelText"));
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
            );
            File file = chooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                iconPath.set(file.getAbsolutePath());
            }
        });

        var addButton = AppButtonFactory.primary(I18n.t("button.add"));
        addButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                notificationLabel.setText(I18n.t("notificationLabel.text"));
                notificationLabel.setVisible(true);
                return;
            }
            notificationLabel.setVisible(false);

            Buff buff = new Buff(
                    name,
                    descriptionField.getText(),
                    typeBox.getValue(),
                    iconPath.get()
            );

            addBuff(buff);

            nameField.clear();
            descriptionField.setText("");
            iconPath.set("");
        });

        HBox controls = new HBox(10, nameField, typeBox, chooseIconButton, addButton);
        getChildren().addAll(title, notificationLabel, controls, descriptionField, listContainer);

        // Если есть initialBuffs, создаем строки
        for (Buff buff : buffs) {
            addBuffRow(buff);
        }
    }

    private void addBuff(Buff buff) {
        buffs.add(buff);
        addBuffRow(buff);
    }

    private void addBuffRow(Buff buff) {
        BuffEditorRow row;
        if (character != null) {
            row = new BuffEditorRow(buff, () -> removeBuff(buff), character);
        } else {
            row = new BuffEditorRow(buff, () -> removeBuff(buff), null);
        }

        listContainer.getChildren().add(row);
    }

    private void removeBuff(Buff buff) {
        listContainer.getChildren().removeIf(node -> node instanceof BuffEditorRow row && row.getBuff() == buff);
        buffs.remove(buff);
    }

    private TextField createStyledTextField() {
        TextField field = new TextField();
        field.setPromptText(I18n.t("buff.promptText.name"));
        field.setStyle("""
                -fx-background-color: %s;
                -fx-text-fill: %s;
                -fx-prompt-text-fill: #aaaaaa;
                -fx-border-color: %s;
                -fx-border-radius: 6;
                -fx-background-radius: 6;
                -fx-padding: 4 6 4 6;
            """.formatted(AppTheme.BACKGROUND_PRIMARY, AppTheme.TEXT_PRIMARY, AppTheme.BORDER_MUTED));
        return field;
    }

    /**
     * Apply buffs to Character object.
     */
    public void applyTo(Character character) {
        character.getBuffs().clear();
        character.getBuffs().addAll(buffs);
    }

    /**
     * Returns current buffs in editor (useful for CharacterCreateScreen).
     */
    public ObservableList<Buff> getBuffs() {
        return FXCollections.unmodifiableObservableList(buffs);
    }
}