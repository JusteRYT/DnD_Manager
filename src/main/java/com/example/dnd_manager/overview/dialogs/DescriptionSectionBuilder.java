package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DescriptionSectionBuilder {

    public VBox createTextBlock(String title, String textContent) {
        Label titleLabel = new Label(title.toUpperCase());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #c89b3c; -fx-font-size: 12px;");

        String effectiveText = textContent != null && !textContent.isBlank()
                ? textContent
                : I18n.t("dialogDescription.emptyValue");
        Label text = new Label(effectiveText);
        text.setWrapText(true);
        text.setStyle("-fx-text-fill: #dddddd; -fx-font-size: 13px; -fx-line-spacing: 3px;");

        VBox box = new VBox(8, titleLabel, text);
        box.setPadding(new Insets(12));
        box.setStyle("""
                -fx-background-color: #2b2b2b;
                -fx-background-radius: 8;
                -fx-border-color: #3a3a3a;
                -fx-border-radius: 8;
                """);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }
}
