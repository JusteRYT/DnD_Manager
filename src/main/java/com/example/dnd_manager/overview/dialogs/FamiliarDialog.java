package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.FamiliarEditor;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FamiliarDialog extends BaseDialog {
    private final Character familiar;
    private final Runnable onSave;
    private FamiliarEditor editor;

    public FamiliarDialog(Stage owner, Character familiar, Runnable onSave) {
        super(owner, "Familiar Editor", 600, 800);
        this.familiar = familiar;
        this.onSave = onSave;
    }

    @Override
    protected void setupContent() {
        editor = new FamiliarEditor(familiar);
        editor.setFocusTraversable(false);

        Button applyBtn = AppButtonFactory.actionSave(I18n.t("button.editSave"));

        applyBtn.setOnAction(e -> {
            editor.applyChanges();
            onSave.run();
            close();
        });
        applyBtn.setFocusTraversable(false);

        Button cancelBtn = AppButtonFactory.actionExit(I18n.t("button.exit"), 100);
        cancelBtn.setOnAction(e -> close());

        HBox buttonContainer = new HBox(15, cancelBtn, applyBtn);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);
        buttonContainer.setStyle("-fx-padding: 10 0 0 0;");

        contentArea.getChildren().addAll(editor, buttonContainer);
    }
}