package com.example.dnd_manager.info.text;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Reusable text section component for character descriptions.
 */
public class TextSection extends VBox {

    private final TextArea textArea;

    public TextSection(String title, int prefRows) {
        setSpacing(5);

        Label label = new Label(title);
        label.setStyle("-fx-font-weight: bold;");

        textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefRowCount(prefRows);

        getChildren().addAll(label, textArea);
    }

    /**
     * @return text content of the section
     */
    public String getText() {
        return textArea.getText();
    }
}
