package com.example.dnd_manager;

import com.example.dnd_manager.screen.ScreenManager;
import com.example.dnd_manager.screen.StartScreen;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.CustomTitleBar;
import com.example.dnd_manager.theme.WindowResizer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    private static final int RESIZE_MARGIN = 7;
    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) {
        log.info("Starting DnD_Manager...");
        log.debug("Operating System: {}", System.getProperty("os.name"));
        log.debug("User Home: {}", System.getProperty("user.home"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        log.debug("Stage style set to UNDECORATED");

        StorageService storageService = new StorageService();
        storageService.init();
        log.info("StorageService initialized successfully.");

        VBox root = new VBox();
        root.setStyle("-fx-border-color: #3a3a3a; -fx-border-width: 1; -fx-background-color: #1e1e1e;");
        root.setPadding(new javafx.geometry.Insets(0, 2, 2, 2));

        CustomTitleBar titleBar = new CustomTitleBar(primaryStage);
        root.getChildren().add(titleBar);
        log.debug("Custom title bar attached.");

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);

        // 3. Используем менеджер для загрузки первого экрана
        log.info("Loading initial screen: StartScreen");
        StartScreen startScreen = new StartScreen(primaryStage, storageService);
        ScreenManager.setScreen(primaryStage, startScreen.getView());

        WindowResizer.listen(primaryStage, RESIZE_MARGIN);
        primaryStage.show();
        log.info("Application window is now visible.");
    }

    public static void main(String[] args) {
        log.debug("Main method called, launching JavaFX...");
        launch();
    }
}