package com.example.dnd_manager.overview.dialogs.common;

import com.example.dnd_manager.application.usecase.character.LevelUpCharacterUseCase;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LevelUpDialog extends BaseDialog {

    private final Character character;
    private final LevelUpCharacterUseCase levelUpCharacterUseCase;
    private final Runnable onLevelUp;

    public LevelUpDialog(Stage owner, Character character, SaveCharacterUseCase saveCharacterUseCase, Runnable onLevelUp) {
        this(owner, character, onLevelUp, new LevelUpCharacterUseCase(saveCharacterUseCase));
    }

    LevelUpDialog(
            Stage owner,
            Character character,
            Runnable onLevelUp,
            LevelUpCharacterUseCase levelUpCharacterUseCase
    ) {
        super(owner, I18n.t("dialogLevel.title"), 400, 180);
        this.character = character;
        this.onLevelUp = onLevelUp;
        this.levelUpCharacterUseCase = levelUpCharacterUseCase;
    }

    @Override
    protected void setupContent() {
        contentArea.setAlignment(Pos.CENTER);
        contentArea.setSpacing(20);

        Label message = new Label(I18n.t("dialogLevel.message"));
        message.setStyle(dialogStyles.messageStyle() + "-fx-text-alignment: center;");
        message.setWrapText(true);

        Button yesBtn = new Button(I18n.t("button.yes"));
        Button noBtn = new Button(I18n.t("button.no"));

        yesBtn.setPrefSize(100, 36);
        noBtn.setPrefSize(100, 36);
        dialogStyles.applyPrimaryButton(yesBtn);
        dialogStyles.applySecondaryButton(noBtn);

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
        levelUpCharacterUseCase.execute(character);

        if (onLevelUp != null) {
            onLevelUp.run();
        }
    }
}












