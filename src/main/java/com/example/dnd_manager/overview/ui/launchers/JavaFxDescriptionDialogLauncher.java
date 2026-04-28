package com.example.dnd_manager.overview.ui.launchers;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.dialogs.common.FullDescriptionDialog;
import javafx.stage.Stage;

public class JavaFxDescriptionDialogLauncher implements DescriptionDialogLauncher {

    @Override
    public void show(Stage owner, Character character) {
        new FullDescriptionDialog(owner, character).show();
    }
}













