package com.example.dnd_manager;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.screen.StartScreen;
import com.example.dnd_manager.store.StorageService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application entry point.
 */
public class MainApp extends Application {


    private static final double INITIAL_WIDTH = 1600;
    private static final double INITIAL_HEIGHT = 1200;

    @Override
    public void start(Stage primaryStage) {
        StorageService storageService = new StorageService();
        storageService.init();

        StartScreen startScreen = new StartScreen(primaryStage, storageService);

        Scene scene = new Scene(startScreen.getView(), 1600, 1200);
        primaryStage.setTitle(I18n.t("title.main"));
        primaryStage.setScene(scene);
        // --- Enforce minimum window size ---
        primaryStage.setMinWidth(INITIAL_WIDTH);
        primaryStage.setMinHeight(INITIAL_HEIGHT);
        primaryStage.show();
    }
}
