package com.example.dnd_manager.overview.ui;

import javafx.stage.Stage;

import java.io.File;

public interface CharacterDescriptionSaveChooser {

    File choose(Stage owner, String characterName);
}
