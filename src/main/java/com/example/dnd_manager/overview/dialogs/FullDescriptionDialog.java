package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Диалог полного описания персонажа.
 * Теперь использует BaseDialog для единого стиля заголовка и рамок.
 */
public class FullDescriptionDialog extends BaseDialog {

    private final Character character;
    private final DescriptionSectionBuilder sectionBuilder;

    public FullDescriptionDialog(Stage owner, Character character) {
        this(owner, character, new DescriptionSectionBuilder());
    }

    FullDescriptionDialog(Stage owner, Character character, DescriptionSectionBuilder sectionBuilder) {
        // Вызываем конструктор базового класса: владелец, заголовок, ширина, высота
        super(owner, character.getName() + " — " + I18n.t("dialogDescription.title"), 550, 450);
        this.character = character;
        this.sectionBuilder = sectionBuilder;
    }

    @Override
    protected void setupContent() {
        // Настраиваем отступы основной области контента (contentArea уже создана в BaseDialog)
        contentArea.setPadding(Insets.EMPTY); // Очистим, так как будем использовать ScrollPane

        VBox textContainer = new VBox(15,
                sectionBuilder.createTextBlock(I18n.t("dialogDescription.textBlock.description"), character.getDescription()),
                sectionBuilder.createTextBlock(I18n.t("dialogDescription.textBlock.personality"), character.getPersonality()),
                sectionBuilder.createTextBlock(I18n.t("dialogDescription.textBlock.backstory"), character.getBackstory())
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
}
