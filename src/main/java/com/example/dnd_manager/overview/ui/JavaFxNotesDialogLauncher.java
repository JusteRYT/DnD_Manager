package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.dialogs.CharacterNotesDialog;
import javafx.stage.Stage;

public class JavaFxNotesDialogLauncher implements NotesDialogLauncher {

    @Override
    public void show(Stage owner, Character character) {
        new CharacterNotesDialog(owner, character).show();
    }
}

