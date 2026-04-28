package com.example.dnd_manager.screen;

import com.example.dnd_manager.theme.scroll.AppScrollPaneFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenManager {
    private static final Logger log = LoggerFactory.getLogger(ScreenManager.class);

    /**
     * Меняет только контентную область, сохраняя TitleBar
     */
    public static void setScreen(Stage stage, Node newView) {
        Parent rootNode = stage.getScene().getRoot();

        if (!(rootNode instanceof VBox root)) {
            log.error("Critical navigation error: expected VBox root with TitleBar");
            log.error("Current root class: {}", rootNode.getClass().getName());
            log.error("Current root id: {}", rootNode.getId());
            log.error("Likely cause: stage.getScene().setRoot() used instead of ScreenManager.setScreen()");
            return;
        }

        if (root.getChildren().size() > 1) {
            root.getChildren().remove(1);
        }

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(newView);
        scrollPane.setFitToHeight(false);
        scrollPane.setFitToWidth(true);

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #1e1e1e; -fx-border-width: 0;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        root.getChildren().add(scrollPane);
    }
}












