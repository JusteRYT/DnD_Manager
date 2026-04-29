package com.example.dnd_manager.theme;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Styled container for displaying a text section.
 * Fully synchronized with AppTextField.
 */
public class AppTextSection extends VBox {

    private final Label titleLabel;
    private final TextArea contentArea;

    public AppTextSection(String title) {
        this(title, "");
    }

    public AppTextSection(String title, String content) {
        super(8);
        setPadding(new Insets(12));

        titleLabel = new Label(title.toUpperCase());
        titleLabel.setStyle("""
            -fx-text-fill: #b7c9dd;
            -fx-font-size: 12px;
            -fx-font-weight: bold;
            -fx-letter-spacing: 1px;
        """);

        contentArea = createStyledTextArea(content, 4, "");

        getChildren().addAll(titleLabel, contentArea);
    }

    public AppTextSection(String initialText, int rows, String promptText) {
        super(0);
        setPadding(Insets.EMPTY);

        titleLabel = null;
        contentArea = createStyledTextArea(initialText, rows, promptText);

        getChildren().add(contentArea);
    }

    private TextArea createStyledTextArea(String content, int rows, String promptText) {
        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setPrefRowCount(rows);
        area.setPromptText(promptText);
        area.setMinHeight(TextArea.USE_PREF_SIZE);

        String baseStyle = """
        -fx-background-color: rgba(75, 93, 127, 0.42), rgba(16, 23, 42, 0.94);
        -fx-background-insets: 0, 1;
        -fx-background-radius: 6;
        -fx-control-inner-background: #10172a;
        -fx-text-fill: #f0f2f7;
        -fx-prompt-text-fill: #8fa4bd;
        -fx-font-size: 13px;
        -fx-focus-color: transparent;
        -fx-faint-focus-color: transparent;
    """;
        String focusStyle = """
        -fx-background-color: rgba(175, 196, 216, 0.72), rgba(16, 23, 42, 0.94);
        -fx-background-insets: 0, 1;
        -fx-background-radius: 6;
        -fx-control-inner-background: #10172a;
        -fx-text-fill: #f0f2f7;
        -fx-prompt-text-fill: #8fa4bd;
        -fx-font-size: 13px;
        -fx-focus-color: transparent;
        -fx-faint-focus-color: transparent;
        -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.18), 12, 0.24, 0, 0);
    """;

        area.setStyle(baseStyle);

        area.focusedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                area.setStyle(focusStyle);
            } else {
                area.setStyle(baseStyle);
            }
        });

        return area;
    }

    public void setText(String content) {
        contentArea.setText(content);
    }

    public String getText() {
        return contentArea.getText().trim();
    }

    public void clear() {
        contentArea.clear();
    }
}











