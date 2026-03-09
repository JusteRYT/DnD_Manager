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
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

@Slf4j
public class AssetManagerScreen extends BorderPane {
    private final Consumer<Path> onAssetSelected;
    private final boolean isPickerMode;

    public AssetManagerScreen(Stage stage, StorageService storageService) {
        this(stage, storageService, null);
    }

    public AssetManagerScreen(Stage stage, StorageService storageService, Consumer<Path> onAssetSelected) {
        this.onAssetSelected = onAssetSelected;
        this.isPickerMode = (onAssetSelected != null);
        log.info("Opening Asset Manager in {} mode", isPickerMode ? "PICKER" : "MANAGER");

        // Главное: заставляем BorderPane растягиваться на всё окно
        setPrefSize(stage.getWidth(), stage.getHeight());
        setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + ";");
        setPadding(new Insets(20));

        // --- Верхняя панель (Заголовок) ---
        Label title = new Label(I18n.t("title.assetManager").toUpperCase());
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + AppTheme.TEXT_ACCENT + ";");

        HBox topBar = new HBox(title);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        setTop(topBar);

        // --- Центральная панель (Вкладки) ---
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Важно: TabPane должен иметь возможность бесконечно расти
        tabPane.setMaxHeight(Double.MAX_VALUE);
        applyTabPaneStyles(tabPane);

        if (isPickerMode) {
            title.setText(I18n.t("title.selectAsset").toUpperCase());
        }

        AssetDnDManager assetDnDManager = new AssetDnDManager();
        Path rootAssetsPath = Paths.get("Assets");

        for (AssetCategory category : AssetCategory.values()) {
            AppCustomTab tab = new AppCustomTab(category, rootAssetsPath, stage, assetDnDManager);
            // Передаем callback в галерею
            tab.getGalleryTab().setPickerMode(onAssetSelected);
            tabPane.getTabs().add(tab);
        }

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab instanceof AppCustomTab customTab) {
                customTab.getGalleryTab().loadImages();
            }
        });

        setCenter(tabPane);

        // --- Нижняя панель (Кнопки) ---
        var backBtn = AppButtonFactory.actionExit(isPickerMode ? I18n.t("button.cancel") : I18n.t("button.exit"), 120);
        backBtn.setOnAction(e -> handleExit(stage, storageService));

        HBox bottomBar = new HBox(backBtn);
        bottomBar.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBar.setPadding(new Insets(20, 0, 0, 0));

        // В BorderPane компонент в setBottom всегда будет в самом низу
        setBottom(bottomBar);
    }

    private void applyTabPaneStyles(TabPane tabPane) {
        tabPane.getStylesheets().add("data:text/css," + """
                .tab-pane {
                    -fx-tab-min-width: 120px;
                }
                .tab-pane .tab-header-area {
                    -fx-padding: 0 0 0 0;
                }
                .tab-pane {\s
                -fx-focus-color: transparent;\s
                -fx-faint-focus-color: transparent;\s
                }
                .tab:focused .tab-label { -fx-focus-color: transparent; }
                .tab .focus-indicator { -fx-focus-color: transparent; -fx-border-color: transparent; -fx-border-width: 0; -fx-background-insets: 0; }
                .tab-pane .tab-header-background {
                    -fx-background-color: transparent;
                }
                .tab {
                    -fx-background-color: #252525;
                    -fx-background-insets: 0 1 0 1;
                    -fx-background-radius: 4 4 0 0;
                    -fx-padding: 10 20 10 20;
                    -fx-cursor: hand;
                }
                .tab:hover {
                    -fx-background-color: #323232;
                }
                .tab:selected {
                    -fx-background-color: %1$s;
                }
                .tab .tab-label {
                    -fx-text-fill: #aaaaaa;
                    -fx-font-size: 13px;
                    -fx-font-weight: bold;
                }
                .tab:selected .tab-label {
                    -fx-text-fill: #1a1a1a;
                }
                /* Убираем синюю полоску фокуса */
                .tab-pane :focused .tab-header-area .headers-region .tab:selected .focus-indicator {
                    -fx-border-color: transparent;
                }
               \s""".formatted(AppTheme.TEXT_ACCENT).replace("\n", ""));
    }

    private void handleExit(Stage stage, StorageService storageService) {
        if (isPickerMode) {
            ((Stage) getScene().getWindow()).close();
        } else {
            ScreenManager.setScreen(stage, new StartScreen(stage, storageService).getView());
        }
    }
}