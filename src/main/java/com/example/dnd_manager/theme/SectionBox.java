package com.example.dnd_manager.theme;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * Styled container for screen sections.
 */
public class SectionBox extends VBox {

    /**
     * Creates a styled section container.
     *
     * @param content section content
     */
    public SectionBox(Node... content) {
        super(12, content);
        setPadding(new Insets(12));
        setStyle("""
            -fx-background-color: %s;
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-border-color: %s;
        """.formatted(
                AppTheme.BACKGROUND_SECONDARY,
                AppTheme.BORDER_MUTED
        ));
    }
}
