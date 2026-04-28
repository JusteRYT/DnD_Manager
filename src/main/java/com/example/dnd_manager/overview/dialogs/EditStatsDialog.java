package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.application.usecase.character.UpdateCharacterStatsUseCase;
import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.dialogs.common.BaseDialog;
import com.example.dnd_manager.theme.IntegerField;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Диалог редактирования основных характеристик персонажа.
 * Наследует стиль MainApp через BaseDialog.
 */
public class EditStatsDialog extends BaseDialog {

    private final Character character;
    private final UpdateCharacterStatsUseCase updateCharacterStatsUseCase;
    private final Runnable refreshCallback;

    public EditStatsDialog(Stage owner, Character character, SaveCharacterUseCase saveCharacterUseCase, Runnable refreshCallback) {
        this(owner, character, refreshCallback, new UpdateCharacterStatsUseCase(saveCharacterUseCase));
    }

    EditStatsDialog(
            Stage owner,
            Character character,
            Runnable refreshCallback,
            UpdateCharacterStatsUseCase updateCharacterStatsUseCase
    ) {
        super(owner, I18n.t("dialogEdit.title"), 400, 480);

        this.character = character;
        this.updateCharacterStatsUseCase = updateCharacterStatsUseCase;
        this.refreshCallback = refreshCallback;
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(15);

        // HP
        Label hpLabel = new Label(I18n.t("hpField.name") + ":");
        hpLabel.setStyle("-fx-text-fill: #ff5555; -fx-font-weight: bold;");
        IntegerField hpField = new IntegerField(String.valueOf(character.getMaxHp()), true);
        VBox hpBox = new VBox(4, hpLabel, hpField.getField());

        // Armor
        Label armorLabel = new Label(I18n.t("armorField.name") + ":");
        armorLabel.setStyle("-fx-text-fill: #55aaff; -fx-font-weight: bold;");
        IntegerField armorField = new IntegerField(String.valueOf(character.getArmor()), true);
        VBox armorBox = new VBox(4, armorLabel, armorField.getField());

        // Mana
        Label manaLabel = new Label(I18n.t("manaField.name") + ":");
        manaLabel.setStyle("-fx-text-fill: #3aa3c3; -fx-font-weight: bold;");
        IntegerField manaField = new IntegerField(String.valueOf(character.getMaxMana()), true);
        VBox manaBox = new VBox(4, manaLabel, manaField.getField());

        // Level
        Label levelLabel = new Label(I18n.t("levelField.name") + ":");
        levelLabel.setStyle("-fx-text-fill: #c89b3c; -fx-font-weight: bold;");
        IntegerField levelField = new IntegerField(String.valueOf(character.getLevel()), true);
        VBox levelBox = new VBox(4, levelLabel, levelField.getField());

        // Кнопка сохранения
        Button saveBtn = AppButtonFactory.actionSave(I18n.t("button.save"));
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setOnAction(ev -> {
            updateCharacterStatsUseCase.execute(
                    character,
                    toNullableInt(hpField),
                    toNullableInt(armorField),
                    toNullableInt(manaField),
                    toNullableInt(levelField)
            );
            if (refreshCallback != null) refreshCallback.run();
            close();
        });

        contentArea.getChildren().addAll(hpBox, armorBox, manaBox, levelBox, saveBtn);
    }

    private Integer toNullableInt(IntegerField field) {
        return field.getText().isBlank() ? null : field.getInt();
    }
}












