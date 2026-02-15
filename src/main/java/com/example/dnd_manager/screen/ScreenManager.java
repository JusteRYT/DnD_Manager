package com.example.dnd_manager.screen;

import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScreenManager {

    /**
     * Меняет только контентную область, сохраняя TitleBar
     */
    public static void setScreen(Stage stage, Node newView) {
        Parent rootNode = stage.getScene().getRoot();

        if (!(rootNode instanceof VBox root)) {
            System.err.println("=== КРИТИЧЕСКАЯ ОШИБКА НАВИГАЦИИ ===");
            System.err.println("Ожидался корень: javafx.scene.layout.VBox (с TitleBar)");
            System.err.println("Текущий корень в сцене: " + rootNode.getClass().getName());
            System.err.println("ID текущего корня: " + rootNode.getId());
            System.err.println("Скорее всего, где-то вызван stage.getScene().setRoot() вместо ScreenManager.setScreen()");
            System.err.println("====================================");
            return;
        }

        if (root.getChildren().size() > 1) {
            root.getChildren().remove(1);
        }

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(newView);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #1e1e1e; -fx-border-width: 0;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.getChildren().add(scrollPane);
    }
}
