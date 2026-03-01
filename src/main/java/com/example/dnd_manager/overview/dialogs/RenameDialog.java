package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class RenameDialog extends BaseDialog {
    private final String oldName;
    private final Consumer<String> onConfirm;
    private TextField inputField;

    public RenameDialog(Stage owner, String oldName, Consumer<String> onConfirm) {
        super(owner, "Rename Asset", 400, 200);
        this.oldName = oldName;
        this.onConfirm = onConfirm;
    }

    @Override
    protected void setupContent() {
        Label label = new Label("Enter new name:");
        label.setStyle("-fx-text-fill: #ccc;");

        inputField = new TextField(oldName);
        inputField.setStyle("""
            -fx-background-color: #2b2b2b;\s
            -fx-text-fill: white;\s
            -fx-border-color: #3a3a3a;
            -fx-padding: 8;
       \s""");

        var saveBtn = AppButtonFactory.actionSave("Save");
        saveBtn.setOnAction(e -> {
            onConfirm.accept(inputField.getText());
            close();
        });

        var cancelBtn = AppButtonFactory.actionExit("Cancel", 80);
        cancelBtn.setOnAction(e -> close());

        HBox buttons = new HBox(10, saveBtn, cancelBtn);
        buttons.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        contentArea.setSpacing(15);
        contentArea.getChildren().addAll(label, inputField, buttons);
    }
}