package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Диалог полного описания персонажа.
 * Теперь использует BaseDialog для единого стиля заголовка и рамок.
 */
public class FullDescriptionDialog extends BaseDialog {

    private final Character character;

    public FullDescriptionDialog(Stage owner, Character character) {
        // Вызываем конструктор базового класса: владелец, заголовок, ширина, высота
        super(owner, character.getName() + " — " + I18n.t("dialogDescription.title"), 550, 450);
        this.character = character;
    }

    @Override
    protected void setupContent() {
        // Настраиваем отступы основной области контента (contentArea уже создана в BaseDialog)
        contentArea.setPadding(Insets.EMPTY); // Очистим, так как будем использовать ScrollPane

        VBox textContainer = new VBox(15,
                createTextBlock(I18n.t("dialogDescription.textBlock.description"), character.getDescription()),
                createTextBlock(I18n.t("dialogDescription.textBlock.personality"), character.getPersonality()),
                createTextBlock(I18n.t("dialogDescription.textBlock.backstory"), character.getBackstory())
        );
        textContainer.setPadding(new Insets(20));
        textContainer.setStyle("-fx-background-color: transparent;");

        // Оборачиваем текст в ScrollPane, так как описание может быть длинным
        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(textContainer);
        scrollPane.setFitToWidth(true);

        // Убираем фон у ScrollPane, чтобы видеть фон диалога
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        contentArea.getChildren().add(scrollPane);
    }

    private VBox createTextBlock(String title, String textContent) {
        Label titleLabel = new Label(title.toUpperCase());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #c89b3c; -fx-font-size: 12px;");

        Label text = new Label(textContent != null && !textContent.isBlank() ? textContent : "...");
        text.setWrapText(true);
        text.setStyle("-fx-text-fill: #dddddd; -fx-font-size: 13px; -fx-line-spacing: 3px;");

        VBox box = new VBox(8, titleLabel, text);
        box.setPadding(new Insets(12));
        box.setStyle("""
                -fx-background-color: #2b2b2b; 
                -fx-background-radius: 8;
                -fx-border-color: #3a3a3a;
                -fx-border-radius: 8;
                """);
        box.setMaxWidth(Double.MAX_VALUE);

        return box;
    }
}