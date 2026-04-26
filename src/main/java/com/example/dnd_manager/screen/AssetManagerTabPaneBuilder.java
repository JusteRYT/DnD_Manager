package com.example.dnd_manager.screen;

import com.example.dnd_manager.assets.AssetCategory;
import com.example.dnd_manager.assets.logic.AssetDnDManager;
import com.example.dnd_manager.theme.AppCustomTab;
import com.example.dnd_manager.theme.AppTheme;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * Builds configured TabPane for asset manager categories.
 */
public class AssetManagerTabPaneBuilder {

    public TabPane build(Stage stage, Consumer<Path> onAssetSelected) {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setMaxHeight(Double.MAX_VALUE);
        applyStyles(tabPane);

        AssetDnDManager assetDnDManager = new AssetDnDManager();
        Path rootAssetsPath = Paths.get("Assets");

        for (AssetCategory category : AssetCategory.values()) {
            AppCustomTab tab = new AppCustomTab(category, rootAssetsPath, stage, assetDnDManager);
            tab.getGalleryTab().setPickerMode(onAssetSelected);
            tabPane.getTabs().add(tab);
        }

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab instanceof AppCustomTab customTab) {
                customTab.getGalleryTab().loadImages();
            }
        });
        return tabPane;
    }

    private void applyStyles(TabPane tabPane) {
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
                .tab-pane :focused .tab-header-area .headers-region .tab:selected .focus-indicator {
                    -fx-border-color: transparent;
                }
               \s""".formatted(AppTheme.TEXT_ACCENT).replace("\n", ""));
    }
}

