package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.lang.I18n;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.MessageFormat;

public class JavaFxCharacterDescriptionSaveChooser implements CharacterDescriptionSaveChooser {

    @Override
    public File choose(Stage owner, String characterName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18n.t("dialog.exportDescription.title"));
        fileChooser.setInitialFileName(
                MessageFormat.format(I18n.t("dialog.exportDescription.defaultFileName"), characterName)
        );
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(I18n.t("dialog.exportDescription.textFiles"), "*.txt")
        );
        return fileChooser.showSaveDialog(owner);
    }
}
