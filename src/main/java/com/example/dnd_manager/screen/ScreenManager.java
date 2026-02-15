package com.example.dnd_manager.screen;

import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScreenManager {

    /**
     * Меняет только контентную область, сохраняя TitleBar
     */
    public static void setScreen(Stage stage, Node newView) {
        VBox root = (VBox) stage.getScene().getRoot();
        if (root.getChildren().size() > 1) {
            root.getChildren().remove(1);
        }

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(newView);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #1e1e1e; -fx-border-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.getChildren().add(scrollPane);
    }
}
