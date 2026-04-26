package com.example.dnd_manager.screen;

import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
public class AssetManagerScreen extends BorderPane {
    private final Consumer<Path> onAssetSelected;
    private final boolean isPickerMode;
    private final AssetManagerController controller;
    private final AssetManagerTabPaneBuilder tabPaneBuilder;

    public AssetManagerScreen(Stage stage, Runnable backToStartAction) {
        this(stage, null, backToStartAction);
    }

    public AssetManagerScreen(Stage stage, Consumer<Path> onAssetSelected) {
        this(
                stage,
                Objects.requireNonNull(onAssetSelected, "onAssetSelected must not be null"),
                () -> {}
        );
    }

    private AssetManagerScreen(Stage stage, Consumer<Path> onAssetSelected, Runnable backToStartAction) {
        this.onAssetSelected = onAssetSelected;
        this.isPickerMode = (onAssetSelected != null);
        this.controller = new AssetManagerController(isPickerMode, backToStartAction);
        this.tabPaneBuilder = new AssetManagerTabPaneBuilder();
        log.info("Opening Asset Manager in {} mode", isPickerMode ? "PICKER" : "MANAGER");

        setPrefSize(stage.getWidth(), stage.getHeight());
        setStyle("-fx-background-color: " + AppTheme.BACKGROUND_PRIMARY + ";");
        setPadding(new Insets(20));

        Label title = new Label(controller.resolveTitle());
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: " + AppTheme.TEXT_ACCENT + ";");

        HBox topBar = new HBox(title);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(0, 0, 20, 0));
        setTop(topBar);

        var tabPane = tabPaneBuilder.build(stage, onAssetSelected);
        setCenter(tabPane);

        var backBtn = AppButtonFactory.actionExit(controller.resolveExitButtonLabel(), 120);
        backBtn.setOnAction(e -> controller.handleExit((Stage) getScene().getWindow()));

        HBox bottomBar = new HBox(backBtn);
        bottomBar.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBar.setPadding(new Insets(20, 0, 0, 0));
        setBottom(bottomBar);
    }
}
