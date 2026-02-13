package com.example.dnd_manager;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.screen.StartScreen;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppScrollPaneFactory;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainApp extends Application {

    // Желаемые размеры для больших мониторов
    private static final double PREFERRED_WIDTH = 1600;
    private static final double PREFERRED_HEIGHT = 1200;

    // Минимально допустимые размеры (ниже которых интерфейс станет нечитаемым)
    private static final double MIN_WIDTH = 1024;
    private static final double MIN_HEIGHT = 768;

    @Override
    public void start(Stage primaryStage) {
        StorageService storageService = new StorageService();
        storageService.init();

        StartScreen startScreen = new StartScreen(primaryStage, storageService);


        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(startScreen.getView());
        scrollPane.setFitToHeight(true);

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        double initialWidth = Math.min(PREFERRED_WIDTH, screenBounds.getWidth() * 0.95);
        double initialHeight = Math.min(PREFERRED_HEIGHT, screenBounds.getHeight() * 0.9);

        Scene scene = new Scene(scrollPane, initialWidth, initialHeight);

        primaryStage.setTitle(I18n.t("title.main"));
        primaryStage.setScene(scene);


        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        // Если монитор совсем маленький (например, старый ноутбук), открываем сразу на весь экран
        if (screenBounds.getWidth() <= MIN_WIDTH || screenBounds.getHeight() <= MIN_HEIGHT) {
            primaryStage.setMaximized(true);
        }

        primaryStage.show();
    }

    public static void main(String[] args) { launch(); }
}