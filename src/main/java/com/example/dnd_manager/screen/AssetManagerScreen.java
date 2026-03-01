package com.example.dnd_manager.screen;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.assets.logic.AssetDnDManager;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.AppCustomTab;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AssetManagerScreen extends BorderPane {
    private static final Logger log = LoggerFactory.getLogger(AssetManagerScreen.class);

    public AssetManagerScreen(Stage stage, StorageService storageService) {
        log.info("Opening Asset Manager Screen");
        setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + ";");
        setPadding(new Insets(20));

        // --- Верхняя панель (Заголовок) ---
        Label title = new Label(I18n.t("title.assetManager").toUpperCase());
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + AppTheme.TEXT_ACCENT + ";");

        HBox topBar = new HBox(title);
        topBar.setAlignment(Pos.CENTER_LEFT); // Выравнивание по левому краю смотрится строже для менеджера
        topBar.setPadding(new Insets(0, 0, 20, 0));
        setTop(topBar);

        // --- Центральная панель (Вкладки) ---
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        applyTabPaneStyles(tabPane);

        Path rootAssetsPath = Paths.get("Assets");
        for (AssetCategory category : AssetCategory.values()) {
            tabPane.getTabs().add(new AppCustomTab(category, rootAssetsPath, stage, new AssetDnDManager()));
        }

        setCenter(tabPane);

        // --- Нижняя панель (Кнопки) ---
        var backBtn = AppButtonFactory.actionExit(I18n.t("button.exit"), 120);
        backBtn.setOnAction(e -> {
            log.debug("Exiting Asset Manager");
            ScreenManager.setScreen(stage, new StartScreen(stage, storageService).getView());
        });

        HBox bottomBar = new HBox(backBtn);
        bottomBar.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBar.setPadding(new Insets(20, 0, 0, 0));
        setBottom(bottomBar);
    }

    private void applyTabPaneStyles(TabPane tabPane) {
        tabPane.setStyle("""
                    -fx-tab-max-height: 40;
                    -fx-tab-min-height: 40;
                """);

        tabPane.getStylesheets().add("data:text/css," +
                ".tab-header-background { -fx-background-color: transparent; }" +
                ".tab-pane *.tab-header-area *.tab-header-background { -fx-background-color: transparent; }" +
                ".tab { -fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-background-radius: 5 5 0 0; }" +
                ".tab:selected { -fx-background-color: " + AppTheme.TEXT_ACCENT + "; }" +
                ".tab:selected .tab-label { -fx-text-fill: #1a1a1a; -fx-font-weight: bold; }");
    }
}