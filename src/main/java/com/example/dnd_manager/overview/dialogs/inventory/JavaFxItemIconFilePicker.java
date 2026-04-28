package com.example.dnd_manager.overview.dialogs.inventory;

import com.example.dnd_manager.lang.I18n;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class JavaFxItemIconFilePicker implements ItemIconFilePicker {

    @Override
    public File pick(Stage owner) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(I18n.t("dialog.inventory.iconChooser.title"));
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        I18n.t("dialog.inventory.iconChooser.filterImages"),
                        "*.png", "*.jpg", "*.jpeg", "*.webp"
                )
        );
        return chooser.showOpenDialog(owner);
    }
}












