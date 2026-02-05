package com.example.dnd_manager.info.text;

import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * Component for base character information.
 */
@Getter
public class BaseInfoForm extends VBox {

    private final TextField nameField = new TextField();
    private final TextField raceField = new TextField();
    private final TextField classField = new TextField();

    public BaseInfoForm() {
        setSpacing(10);
        setPadding(new Insets(10));

        styleTextField(nameField, "Name");
        styleTextField(raceField, "Race");
        styleTextField(classField, "Class");

        getChildren().addAll(nameField, raceField, classField);
    }

    /**
     * Applies theme styling to a text field.
     *
     * @param field      the TextField to style
     * @param promptText placeholder text
     */
    private void styleTextField(TextField field, String promptText) {
        field.setPrefWidth(1500);
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

    public String getName() {
        return nameField.getText();
    }

    public String getRace() {
        return raceField.getText();
    }

    public String getCharacterClass() {
        return classField.getText();
    }
}