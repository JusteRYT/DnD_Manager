package com.example.dnd_manager;

import com.example.dnd_manager.screen.StartScreen;
import com.example.dnd_manager.store.StorageService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application entry point.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        StorageService storageService = new StorageService();
        storageService.init();

        StartScreen startScreen = new StartScreen(primaryStage, storageService);

        Scene scene = new Scene(startScreen.getView(), 1600, 900);
        primaryStage.setTitle("D&D Character Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
