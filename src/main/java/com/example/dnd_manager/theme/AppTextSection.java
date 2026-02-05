package com.example.dnd_manager.theme;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Styled container for displaying a text section.
 * Background and text colors follow the application theme.
 * Can be created with or without title and border.
 */
public class AppTextSection extends VBox {

    private final Label titleLabel;
    private final TextArea contentArea;

    /**
     * Creates a text section with a title and no initial content.
     *
     * @param title section title
     */
    public AppTextSection(String title) {
        this(title, "");
    }

    /**
     * Creates a text section with a title and initial content.
     *
     * @param title   section title
     * @param content section text content
     */
    public AppTextSection(String title, String content) {
        super(8); // spacing between title and content
        setPadding(new Insets(12));
        setStyle("""
            -fx-background-color: %s;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-border-color: %s;
        """.formatted(AppTheme.BACKGROUND_SECONDARY, AppTheme.BORDER_MUTED));

        titleLabel = new Label(title);
        titleLabel.setStyle("""
            -fx-text-fill: %s;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
        """.formatted(AppTheme.TEXT_ACCENT));

        contentArea = createStyledTextArea(content, 4, "");

        getChildren().addAll(titleLabel, contentArea);
    }

    /**
     * Creates a “plain” text area without title or border.
     *
     * @param initialText initial text content
     * @param rows        preferred row count
     */
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
        area.setStyle("""
            -fx-control-inner-background: %s;
            -fx-text-fill: %s;
            -fx-font-size: 12px;
            -fx-prompt-text-fill: #aaaaaa;
        """.formatted(AppTheme.BACKGROUND_PRIMARY, AppTheme.TEXT_PRIMARY));
        return area;
    }

    /**
     * Updates the content text of the section.
     *
     * @param content new content text
     */
    public void setText(String content) {
        contentArea.setText(content);
    }

    /**
     * Returns the current content text.
     *
     * @return content text
     */
    public String getText() {
        return contentArea.getText();
    }

    /**
     * Updates the title text of the section.
     *
     * @param title new title text
     */
    public void setTitle(String title) {
        if (titleLabel != null) titleLabel.setText(title);
    }

    /**
     * Returns the section title.
     *
     * @return section title
     */
    public String getTitle() {
        return titleLabel != null ? titleLabel.getText() : null;
    }
}