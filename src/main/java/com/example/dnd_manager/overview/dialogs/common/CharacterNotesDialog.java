package com.example.dnd_manager.overview.dialogs.common;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.application.service.NotesService;
import com.example.dnd_manager.theme.WindowResizer;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.util.Objects;

public class CharacterNotesDialog extends BaseDialog {

    private final Character character;
    private final NotesService notesService;
    private TextArea textArea;

    public CharacterNotesDialog(Stage owner, Character character) {
        this(owner, character, new NotesService());
    }

    CharacterNotesDialog(Stage owner, Character character, NotesService notesService) {
        super(owner, I18n.t("title.notes") + ": " + character.getName(), 600, 500);
        this.character = character;
        this.notesService = Objects.requireNonNull(notesService);

        this.stage.setMinWidth(400);
        this.stage.setMinHeight(300);
    }

    @Override
    protected void setupContent() {
        textArea = new TextArea();
        textArea.setText(notesService.loadNotes(character.getName()));
        textArea.setWrapText(true);
        textArea.setPromptText(I18n.t("prompt.notesPlaceholder"));
        dialogStyles.applyTextInput(textArea);

        VBox.setVgrow(textArea, Priority.ALWAYS);

        Button saveBtn = new Button(I18n.t("button.save"));
        saveBtn.setPrefSize(130, 36);
        dialogStyles.applyPrimaryButton(saveBtn);
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












