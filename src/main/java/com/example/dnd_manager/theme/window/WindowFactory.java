package com.example.dnd_manager.theme.window;

import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.CustomTitleBar;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowFactory {

    public static void openModal(Stage owner, Node content, double width, double height) {
        Stage modalStage = new Stage();
        modalStage.initOwner(owner);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.UNDECORATED);

        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + "; " +
                "-fx-border-color: " + AppTheme.BORDER_ACCENT + "; -fx-border-width: 1; -fx-border-radius: 5;");

        CustomTitleBar titleBar = new CustomTitleBar(modalStage);

        VBox.setVgrow(content, Priority.ALWAYS);

        root.getChildren().addAll(titleBar, content);

        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
        modalStage.setScene(scene);

        modalStage.showAndWait();
    }
}











