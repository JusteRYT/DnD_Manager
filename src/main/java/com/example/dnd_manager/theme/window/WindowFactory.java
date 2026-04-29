package com.example.dnd_manager.theme.window;

import com.example.dnd_manager.theme.CustomTitleBar;
import com.example.dnd_manager.theme.dialog.AppDialogStyleProvider;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowFactory {

    private static final AppDialogStyleProvider DIALOG_STYLES = new AppDialogStyleProvider();

    public static void openModal(Stage owner, Node content, double width, double height) {
        Stage modalStage = new Stage();
        modalStage.initOwner(owner);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox();
        root.setStyle(DIALOG_STYLES.rootStyle());

        CustomTitleBar titleBar = new CustomTitleBar(modalStage);

        VBox.setVgrow(content, Priority.ALWAYS);

        root.getChildren().addAll(titleBar, content);

        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
        modalStage.setScene(scene);

        modalStage.showAndWait();
    }
}











