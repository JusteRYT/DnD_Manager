package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.service.NotesService;
import com.example.dnd_manager.theme.WindowResizer;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class CharacterNotesDialog extends BaseDialog {

    private final Character character;
    private final NotesService notesService = new NotesService();
    private TextArea textArea;

    public CharacterNotesDialog(Stage owner, Character character) {
        super(owner, I18n.t("title.notes") + ": " + character.getName(), 600, 500);
        this.character = character;

        this.stage.setMinWidth(400);
        this.stage.setMinHeight(300);
    }

    @Override
    protected void setupContent() {
        textArea = new TextArea();
        textArea.setText(notesService.loadNotes(character.getName()));
        textArea.setWrapText(true);
        textArea.setPromptText(I18n.t("prompt.notesPlaceholder"));

        textArea.setStyle("""
            -fx-control-inner-background: #2b2b2b;
            -fx-text-fill: #dcdcdc;
            -fx-font-size: 14px;
            -fx-prompt-text-fill: #666666;
            -fx-background-color: transparent;
            -fx-border-color: #3a3a3a;
            -fx-border-radius: 4;
            -fx-focus-color: transparent;
            -fx-faint-focus-color: transparent;
        """);

        VBox.setVgrow(textArea, Priority.ALWAYS);

        Button saveBtn = AppButtonFactory.actionSave(I18n.t("button.save"));
        saveBtn.setOnAction(e -> {
            notesService.saveNotes(character.getName(), textArea.getText());
            close();
        });

        HBox buttonBox = new HBox(saveBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setStyle("-fx-padding: 10 0 0 0;");

        contentArea.getChildren().addAll(textArea, buttonBox);

        WindowResizer.listen(this.stage, 10);
    }
}