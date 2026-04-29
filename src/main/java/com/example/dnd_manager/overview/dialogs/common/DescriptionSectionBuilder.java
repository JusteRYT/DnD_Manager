package com.example.dnd_manager.overview.dialogs.common;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.dialog.AppDialogStyleProvider;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DescriptionSectionBuilder {

    private final AppDialogStyleProvider styles = new AppDialogStyleProvider();

    public VBox createTextBlock(String title, String textContent) {
        Label titleLabel = new Label(title.toUpperCase());
        titleLabel.setStyle(styles.sectionTitleStyle());

        String effectiveText = textContent != null && !textContent.isBlank()
                ? textContent
                : I18n.t("dialogDescription.emptyValue");
        Label text = new Label(effectiveText);
        text.setWrapText(true);
        text.setStyle(styles.sectionTextStyle());

        VBox box = new VBox(8, titleLabel, text);
        box.setPadding(new Insets(12));
        box.setStyle(styles.panelStyle());
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }
}












