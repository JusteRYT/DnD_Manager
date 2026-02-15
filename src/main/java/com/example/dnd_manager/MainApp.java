package com.example.dnd_manager;

import com.example.dnd_manager.screen.StartScreen;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.CustomTitleBar;
import com.example.dnd_manager.theme.WindowResizer;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

    private static final double PREFERRED_WIDTH = 1600;
    private static final double PREFERRED_HEIGHT = 1200;
    private static final double MIN_WIDTH = 1024;
    private static final double MIN_HEIGHT = 768;
    private static final int RESIZE_MARGIN = 7;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);

        StorageService storageService = new StorageService();
        storageService.init();

        StartScreen startScreen = new StartScreen(primaryStage, storageService);

        VBox root = new VBox();
        root.setStyle("-fx-border-color: #3a3a3a; -fx-border-width: 1; -fx-background-color: #1e1e1e;");
        root.setPadding(new javafx.geometry.Insets(0, 2, 2, 2));

        CustomTitleBar titleBar = new CustomTitleBar(primaryStage);

        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(startScreen.getView());
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.getChildren().addAll(titleBar, scrollPane);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double initialWidth = Math.min(PREFERRED_WIDTH, screenBounds.getWidth() * 0.95);
        double initialHeight = Math.min(PREFERRED_HEIGHT, screenBounds.getHeight() * 0.9);

        Scene scene = new Scene(root, initialWidth, initialHeight);
        primaryStage.setScene(scene);

        // --- Вызов логики Resize через статический метод ---
        WindowResizer.listen(primaryStage, RESIZE_MARGIN);

        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        if (screenBounds.getWidth() <= MIN_WIDTH || screenBounds.getHeight() <= MIN_HEIGHT) {
            primaryStage.setMaximized(true);
        }

        primaryStage.show();
    }

    public static void main(String[] args) { launch(); }
}