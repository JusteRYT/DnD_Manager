package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.screen.CharacterOverviewScreen;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Full description dialog
 */
public class FullDescriptionDialog {

    private final Character character;
    private final Stage stage;

    public FullDescriptionDialog(Character character, CharacterOverviewScreen parentScreen) {
        this.character = character;
        this.stage = new Stage();
        stage.initOwner(parentScreen.getScene().getWindow());
        stage.setTitle(character.getName() + I18n.t("dialogDescription.title"));
    }

    public void show() {
        // Контейнер с контентом
        VBox content = new VBox(15,
                createTextBlock(I18n.t("dialogDescription.textBlock.description"), character.getDescription()),
                createTextBlock(I18n.t("dialogDescription.textBlock.personality"), character.getPersonality()),
                createTextBlock(I18n.t("dialogDescription.textBlock.backstory"), character.getBackstory())
        );
        content.setPadding(new Insets(15));
        content.setStyle("-fx-background-color: #1e1e1e; -fx-background-radius: 8;");

        // ScrollPane с фоном приложения
        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(content);

        // Основной контейнер сцены с фоном приложения
        StackPane root = new StackPane(scrollPane);
        //root.setStyle("-fx-background-color: #1e1e1e;");

        Scene scene = new Scene(root, 500, 400);
        scene.setFill(javafx.scene.paint.Color.web("#1e1e1e")); // фон сцены

        stage.setScene(scene);
        stage.show();
    }

    private VBox createTextBlock(String title, String textContent) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-font-size: 14px;");

        Label text = new Label(textContent != null ? textContent : "");
        text.setWrapText(true);
        text.setStyle("-fx-text-fill: #dddddd; -fx-font-size: 13px;");

        VBox box = new VBox(5, titleLabel, text);
        box.setPadding(new Insets(8));
        box.setStyle("-fx-background-color: #2b2b2b; -fx-background-radius: 6;");
        box.setMaxWidth(Double.MAX_VALUE);

        return box;
    }
}
