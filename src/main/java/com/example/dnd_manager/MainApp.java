package com.example.dnd_manager;

import com.example.dnd_manager.screen.ScreenManager;
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

    private static final int RESIZE_MARGIN = 7;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);

        StorageService storageService = new StorageService();
        storageService.init();

        VBox root = new VBox();
        root.setStyle("-fx-border-color: #3a3a3a; -fx-border-width: 1; -fx-background-color: #1e1e1e;");
        root.setPadding(new javafx.geometry.Insets(0, 2, 2, 2));

        CustomTitleBar titleBar = new CustomTitleBar(primaryStage);
        root.getChildren().add(titleBar);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);

        // 3. Используем менеджер для загрузки первого экрана
        StartScreen startScreen = new StartScreen(primaryStage, storageService);
        ScreenManager.setScreen(primaryStage, startScreen.getView());

        WindowResizer.listen(primaryStage, RESIZE_MARGIN);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(); }
}