package com.example.dnd_manager.theme.factory;

import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.CustomTitleBar; // Твой класс титлбара
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
        modalStage.initStyle(StageStyle.UNDECORATED); // Убираем стандартные рамки Windows

        // Создаем корень окна, как в главном окне
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + "; " +
                "-fx-border-color: #4a4a4a; -fx-border-width: 1; -fx-border-radius: 5;");

        // Добавляем твой кастомный TitleBar
        CustomTitleBar titleBar = new CustomTitleBar(modalStage);

        // Оборачиваем контент в ScrollPane или просто добавляем (AssetManagerScreen сам BorderPane)
        VBox.setVgrow(content, Priority.ALWAYS);

        root.getChildren().addAll(titleBar, content);

        Scene scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
        modalStage.setScene(scene);

        modalStage.showAndWait();
    }
}