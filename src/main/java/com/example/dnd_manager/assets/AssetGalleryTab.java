package com.example.dnd_manager.assets;

import com.example.dnd_manager.assets.logic.AssetActionHandler;
import com.example.dnd_manager.assets.logic.AssetDnDManager;
import com.example.dnd_manager.assets.logic.AssetSelectionModel;
import com.example.dnd_manager.assets.ui.AssetCard;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class AssetGalleryTab extends VBox {
    private static final Logger log = LoggerFactory.getLogger(AssetGalleryTab.class);

    private final Path rootCategoryPath;
    private final FlowPane galleryPane;
    private final Stage stage;
    private Thread loadingThread;

    private final AssetSelectionModel selectionModel = new AssetSelectionModel();
    private final AssetActionHandler actionHandler;
    private final AssetDnDManager dndManager;
    private final AssetCategory category;
    private final Path baseAssetsPath;

    public AssetGalleryTab(AssetCategory category, Path basePath, Stage stage, AssetDnDManager dndManager) {
        this.category = category;
        this.baseAssetsPath = basePath;
        this.stage = stage;
        this.rootCategoryPath = category.isAll() ? baseAssetsPath : baseAssetsPath.resolve(category.getFolderName());
        this.dndManager = dndManager;
        this.actionHandler = new AssetActionHandler(this::loadImages, stage);

        // Настройка контейнера: убираем лишние отступы, чтобы занять всё пространство
        setSpacing(15);
        setPadding(new Insets(10, 0, 0, 0));
        setFillWidth(true); // VBox будет растягивать детей по горизонтали
        setMaxHeight(Double.MAX_VALUE);
        setStyle("-fx-background-color: transparent;");

        // Создаем папку категории, если её нет
        try {
            Files.createDirectories(rootCategoryPath);
        } catch (Exception e) {
            log.error("Dir error", e);
        }

        Button uploadBtn = AppButtonFactory.actionAdd("Add assets", 150);
        uploadBtn.setOnAction(e -> handleUpload());

        if (category.isAll()) {
            uploadBtn.setVisible(false);
            uploadBtn.setManaged(false);
        }

        HBox controls = new HBox(20, uploadBtn);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(0, 10, 0, 10));

        // --- Сетка галереи ---
        galleryPane = new FlowPane(15, 15);
        galleryPane.setAlignment(Pos.TOP_LEFT);
        galleryPane.setPadding(new Insets(10));

        // Клик по пустому месту сбрасывает выделение через модель
        galleryPane.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getTarget() == galleryPane) {
                selectionModel.clear();
            }
        });

        // --- Скролл и растяжение ---
        ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(galleryPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");

        // Позволяем ScrollPane забирать всё свободное место в VBox
        VBox.setVgrow(this, Priority.ALWAYS);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Биндинг ширины для корректного переноса иконок
        galleryPane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(20));

        getChildren().addAll(controls, scrollPane);

        loadImages();
    }

    public void loadImages() {
        if (loadingThread != null && loadingThread.isAlive()) loadingThread.interrupt();

        Platform.runLater(() -> {
            selectionModel.clear();
            galleryPane.getChildren().clear();
        });

        loadingThread = new Thread(() -> {
            try {
                Stream<Path> stream;
                if (category.isAll()) {
                    stream = Files.walk(baseAssetsPath, 2);
                } else {
                    stream = Files.list(rootCategoryPath);
                }

                stream.filter(Files::isRegularFile)
                        .filter(p -> p.toString().toLowerCase().matches(".*\\.(png|jpg|jpeg|webp)$"))
                        .distinct()
                        .forEach(path -> {
                            if (Thread.currentThread().isInterrupted()) return;
                            Image img = new Image(path.toUri().toString(), 150, 150, true, true, true);
                            Platform.runLater(() -> {
                                AssetCard card = new AssetCard(path, img, selectionModel, actionHandler, dndManager, this::loadImages);
                                galleryPane.getChildren().add(card);
                            });
                        });
            } catch (Exception e) {
                log.error("Load error", e);
            }
        });
        loadingThread.setDaemon(true);
        loadingThread.start();
    }

    private void handleUpload() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp"));
        var files = chooser.showOpenMultipleDialog(stage);
        if (files != null) {
            for (File file : files) {
                try {
                    Files.copy(file.toPath(), rootCategoryPath.resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    log.error("Copy error", ex);
                }
            }
            loadImages();
        }
    }
}