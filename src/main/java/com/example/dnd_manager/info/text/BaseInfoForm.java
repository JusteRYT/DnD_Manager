package com.example.dnd_manager.info.text;

import com.example.dnd_manager.info.text.dto.BaseInfoData;
import com.example.dnd_manager.screen.FormMode;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Component for base character information.
 * Supports CREATE and EDIT modes.
 */
public class BaseInfoForm extends VBox {

    private final TextField nameField = new TextField();
    private final TextField raceField = new TextField();
    private final TextField classField = new TextField();
    private final TextField hpField = new TextField();
    private final TextField armorField = new TextField();
    private final TextField manaField = new TextField();
    private final TextField levelField = new TextField();

    private final FormMode mode;

    /**
     * Constructor for CREATE mode.
     */
    public BaseInfoForm() {
        this(FormMode.CREATE, null);
    }

    public BaseInfoForm(FormMode mode, BaseInfoData data) {
        this.mode = mode;

        setSpacing(10);
        setPadding(new Insets(20, 0, 0, 0));

        styleTextField(nameField, "Name");
        styleTextField(raceField, "Race");
        styleTextField(classField, "Class");
        styleTextField(hpField, "HP");
        styleTextField(armorField, "Armor");
        styleTextField(manaField, "Mana");
        styleTextField(levelField, "Level");

        if (mode == FormMode.EDIT && data != null) {
            applyEditData(data);
        }

        configureByMode();

        getChildren().addAll(nameField, raceField, classField, hpField, armorField,  manaField,  levelField);
    }

    /**
     * Reads current form state as immutable data object.
     *
     * @return base info data
     */
    public BaseInfoData getData() {
        return new BaseInfoData(
                nameField.getText().trim(),
                raceField.getText().trim(),
                classField.getText().trim(),
                hpField.getText().trim(),
                armorField.getText().trim(),
                manaField.getText().trim(),
                levelField.getText().trim()
        );
    }

    /**
     * Applies initial values for EDIT mode.
     */
    private void applyEditData(BaseInfoData data) {
        nameField.setText(data.name());
        raceField.setText(data.race());
        classField.setText(data.characterClass());
        hpField.setText(data.hp());
        armorField.setText(data.armor());
        manaField.setText(data.mana());
        levelField.setText(data.level());
    }

    /**
     * Configures form behavior based on mode.
     */
    private void configureByMode() {
        if (mode == FormMode.EDIT) {
            // Обычно имя персонажа — идентификатор
            nameField.setDisable(true);
        }
    }

    /**
     * Applies theme styling to a text field.
     */
    private void styleTextField(TextField field, String promptText) {
        field.setPrefWidth(500);
        field.setPromptText(promptText);
        field.setStyle("""
            -fx-background-color: %s;
            -fx-text-fill: %s;
            -fx-prompt-text-fill: #aaaaaa;
            -fx-border-color: %s;
            -fx-border-radius: 6;
            -fx-background-radius: 6;
            -fx-padding: 6 8 6 8;
        """.formatted(
                AppTheme.BACKGROUND_SECONDARY,
                AppTheme.TEXT_PRIMARY,
                AppTheme.BORDER_MUTED
        ));
    }
}