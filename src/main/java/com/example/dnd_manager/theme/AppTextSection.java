package com.example.dnd_manager.theme;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Styled container for displaying a text section.
 * Полностью синхронизирован по стилю с AppTextField.
 */
public class AppTextSection extends VBox {

    private final Label titleLabel;
    private final TextArea contentArea;

    public AppTextSection(String title) {
        this(title, "");
    }

    public AppTextSection(String title, String content) {
        super(8); // Отступ между заголовком и полем
        setPadding(new Insets(12));

        // Стилизуем заголовок в стиле DnD (как в BaseInfoForm)
        titleLabel = new Label(title.toUpperCase());
        titleLabel.setStyle("""
            -fx-text-fill: #c89b3c;
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

        // Добавляем -fx-control-inner-background: #1e1e1e;
        // Это закрасит ту самую белую область
        String baseStyle = """
        -fx-background-color: #3a3a3a, #1e1e1e;
        -fx-background-insets: 0, 1;
        -fx-background-radius: 6;
        -fx-control-inner-background: #1e1e1e;
        -fx-text-fill: #eee;
        -fx-prompt-text-fill: #444;
        -fx-font-size: 13px;
        -fx-focus-color: transparent;
        -fx-faint-focus-color: transparent;
    """;

        area.setStyle(baseStyle);

        area.focusedProperty().addListener((obs, old, newVal) -> {
            if (newVal) {
                area.setStyle(baseStyle.replace("-fx-background-color: #3a3a3a, #1e1e1e;",
                        "-fx-background-color: #FFC107, #1e1e1e;")
                        + "-fx-effect: dropshadow(three-pass-box, rgba(255,193,7,0.1), 10, 0, 0, 0);");
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

    public void setTitle(String title) {
        if (titleLabel != null) titleLabel.setText(title.toUpperCase());
    }

    public String getTitle() {
        return titleLabel != null ? titleLabel.getText() : null;
    }

    public void clear() {
        contentArea.clear();
    }
}