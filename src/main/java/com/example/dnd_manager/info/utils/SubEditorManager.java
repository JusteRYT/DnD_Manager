package com.example.dnd_manager.info.utils;

import com.example.dnd_manager.info.editors.AbstractEntityEditor;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class SubEditorManager {

    public static <E> void open(Stage owner,
                                AbstractEntityEditor<E> editor,
                                List<E> targetList,
                                String title,
                                Runnable onApply) {
        Stage subStage = new Stage();
        subStage.initModality(Modality.APPLICATION_MODAL);
        if (owner != null) subStage.initOwner(owner);
        subStage.setTitle(title);

        editor.getItems().setAll(targetList);
        editor.refreshUI();

        Button saveBtn = AppButtonFactory.actionSave(I18n.t("button.applyToItem"));
        saveBtn.setPrefWidth(200);
        saveBtn.setOnAction(e -> {
            targetList.clear();
            targetList.addAll(editor.getItems());
            if (onApply != null) onApply.run();
            subStage.close();
        });

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(editor);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        VBox layout = new VBox(15, scrollPane, saveBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #1e1e1e;");

        Scene scene = new Scene(layout, 600, 700);
        subStage.setScene(scene);
        subStage.showAndWait();
    }
}
