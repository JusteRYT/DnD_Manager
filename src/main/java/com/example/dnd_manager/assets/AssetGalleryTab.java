package com.example.dnd_manager.assets;

import com.example.dnd_manager.assets.logic.AssetActionHandler;
import com.example.dnd_manager.assets.logic.AssetDnDManager;
import com.example.dnd_manager.assets.logic.AssetGalleryController;
import com.example.dnd_manager.assets.logic.AssetSelectionModel;
import com.example.dnd_manager.assets.service.AssetGalleryService;
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
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.function.Consumer;

public class AssetGalleryTab extends VBox {
    private static final Logger log = LoggerFactory.getLogger(AssetGalleryTab.class);

    private final FlowPane galleryPane;
    private Thread loadingThread;

    private final AssetSelectionModel selectionModel = new AssetSelectionModel();
    private final AssetActionHandler actionHandler;
    private final AssetDnDManager dndManager;
    private final AssetGalleryController galleryController;
    private Consumer<Path> selectionCallback;

    public AssetGalleryTab(AssetCategory category, Path basePath, Stage stage, AssetDnDManager dndManager) {
        this.galleryController = new AssetGalleryController(
                category,
                basePath,
                stage,
                new AssetGalleryService()
        );
        this.dndManager = dndManager;
        this.actionHandler = new AssetActionHandler(this::loadImages, stage);

        // Настройка контейнера: убираем лишние отступы, чтобы занять всё пространство
        setSpacing(15);
        setPadding(new Insets(10, 0, 0, 0));
        setFillWidth(true); // VBox будет растягивать детей по горизонтали
        setMaxHeight(Double.MAX_VALUE);
        setStyle("-fx-background-color: transparent;");

        galleryController.ensureCategoryDirectory();

        Button uploadBtn = AppButtonFactory.actionAdd("Add assets", 150);
        uploadBtn.setOnAction(e -> galleryController.handleUpload(this::loadImages));

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

    public void setPickerMode(Consumer<Path> onAssetSelected) {
        selectionCallback = onAssetSelected;
    }

    /**
     * Loads images from the specified category directory.
     * Interrupts any ongoing loading process before starting a new one.
     */
    public void loadImages() {
        if (loadingThread != null && loadingThread.isAlive()) {
            loadingThread.interrupt();
        }

        Platform.runLater(() -> {
            selectionModel.clear();
            galleryPane.getChildren().clear();
        });

        loadingThread = galleryController.startLoadingImages(
                this::addCard,
                e -> log.error("Failed to load images", e)
        );
    }

    /**
     * Creates and adds an asset card to the gallery.
     * Loads the image synchronously to prevent JavaFX thread pool exhaustion.
     *
     * @param path the path to the image file
     */
    private void addCard(Path path) {
        if (Thread.currentThread().isInterrupted()) {
            return;
        }

        Image image = new Image(
                path.toUri().toString(),
                150,
                150,
                true,
                true,
                false
        );

        Thread loadingThreadRef = Thread.currentThread();

        Platform.runLater(() -> {
            if (loadingThreadRef.isInterrupted()) {
                return;
            }

            AssetCard card = new AssetCard(
                    path,
                    image,
                    selectionModel,
                    actionHandler,
                    dndManager,
                    this::loadImages,
                    selectionCallback
            );

            galleryPane.getChildren().add(card);
        });
    }

}
