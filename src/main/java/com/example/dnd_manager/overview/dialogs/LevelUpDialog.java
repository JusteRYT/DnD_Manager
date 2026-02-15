package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LevelUpDialog extends BaseDialog {

    private final Character character;
    private final StorageService storageService;
    private final Runnable onLevelUp;

    public LevelUpDialog(Stage owner, Character character, StorageService storageService, Runnable onLevelUp) {
        super(owner, I18n.t("dialogLevel.title"), 400, 180);
        this.character = character;
        this.storageService = storageService;
        this.onLevelUp = onLevelUp;
    }

    @Override
    protected void setupContent() {
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setSpacing(20);

        Label message = new Label(I18n.t("dialogLevel.message"));
        message.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-text-alignment: center;");
        message.setWrapText(true);

        Button yesBtn = AppButtonFactory.actionImport(I18n.t("button.yes"), 120);
        Button noBtn = AppButtonFactory.actionExit(I18n.t("button.no"), 120);

        yesBtn.setPrefWidth(80);
        noBtn.setPrefWidth(80);

        yesBtn.setOnAction(ev -> {
            performLevelUp();
            close();
        });

        noBtn.setOnAction(ev -> close());

        HBox buttons = new HBox(15, yesBtn, noBtn);
        buttons.setAlignment(Pos.CENTER);

        contentArea.getChildren().addAll(message, buttons);
    }

    private void performLevelUp() {
        int currentLevel = character.getLevel();
        character.setLevel(currentLevel + 1);
        storageService.saveCharacter(character);

        if (onLevelUp != null) {
            onLevelUp.run();
        }
    }
}