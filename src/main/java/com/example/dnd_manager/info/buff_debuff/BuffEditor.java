package com.example.dnd_manager.info.buff_debuff;

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
import lombok.Getter;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Editor component for character buffs and debuffs.
 * Fully styled according to application theme.
 */
@Getter
public class BuffEditor extends VBox {

    private final ObservableList<Buff> buffs = FXCollections.observableArrayList();
    private final VBox listContainer = new VBox(5);

    public BuffEditor() {
        setSpacing(10);

        // Заголовок
        Label title = new Label("Buffs / Debuffs");
        title.setStyle("""
                    -fx-text-fill: %s;
                    -fx-font-weight: bold;
                    -fx-font-size: 14px;
                """.formatted(AppTheme.TEXT_ACCENT));

        // Поля ввода
        TextField nameField = createStyledTextField();
        AppTextSection descriptionField = new AppTextSection("", 4, "Description");
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
        Button chooseIconButton = AppButtonFactory.customButton("Icon", 60);
        chooseIconButton.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Buff Icon");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
            );
            File file = chooser.showOpenDialog(getScene().getWindow());
            if (file != null) {
                iconPath.set(file.getAbsolutePath());
            }
        });

        var addButton = AppButtonFactory.customButton("Add", 60);
        addButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                notificationLabel.setText("Name is required!");
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

            buffs.add(buff);

            BuffEditorRow row = new BuffEditorRow(buff, () -> removeBuffRow(buff));
            listContainer.getChildren().add(row);

            nameField.clear();
            descriptionField.setText("");
            iconPath.set("");
        });

        HBox controls = new HBox(10, nameField, typeBox, chooseIconButton, addButton);
        getChildren().addAll(title, notificationLabel, controls, descriptionField, listContainer);
    }

    private TextField createStyledTextField() {
        TextField field = new TextField();
        field.setPromptText("Name");
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


    private void removeBuffRow(Buff buff) {
        listContainer.getChildren().stream()
                .filter(node -> node instanceof BuffEditorRow row && row.getBuff() == buff)
                .findFirst()
                .ifPresent(node -> {
                    node.setManaged(false);
                    node.setVisible(false);
                    buffs.remove(buff);
                });
    }
}