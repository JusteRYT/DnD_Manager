package com.example.dnd_manager.assets;

import com.example.dnd_manager.assets.logic.AssetActionHandler;
import com.example.dnd_manager.assets.logic.AssetDnDManager;
import com.example.dnd_manager.assets.logic.AssetGalleryController;
import com.example.dnd_manager.assets.logic.AssetGalleryLoadingCoordinator;
import com.example.dnd_manager.assets.logic.AssetSelectionModel;
import com.example.dnd_manager.assets.service.AssetGalleryService;
import com.example.dnd_manager.assets.ui.AssetCard;
import com.example.dnd_manager.assets.ui.AssetCardFactory;
import com.example.dnd_manager.assets.ui.AssetGalleryTabStyleProvider;
import com.example.dnd_manager.assets.ui.AssetGalleryView;
import com.example.dnd_manager.assets.ui.AssetGalleryViewBuilder;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
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

    private final AssetSelectionModel selectionModel = new AssetSelectionModel();
    private final AssetActionHandler actionHandler;
    private final AssetDnDManager dndManager;
    private final AssetGalleryController galleryController;
    private final AssetCardFactory cardFactory = new AssetCardFactory();
    private final AssetGalleryLoadingCoordinator loadingCoordinator = new AssetGalleryLoadingCoordinator();
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
        AssetGalleryTabStyleProvider styleProvider = new AssetGalleryTabStyleProvider();
        AssetGalleryViewBuilder viewBuilder = new AssetGalleryViewBuilder(styleProvider);

        // Настройка контейнера: убираем лишние отступы, чтобы занять всё пространство
        setSpacing(15);
        setPadding(new Insets(10, 0, 0, 0));
        setFillWidth(true); // VBox будет растягивать детей по горизонтали
        setMaxHeight(Double.MAX_VALUE);
        setStyle(styleProvider.rootStyle());

        galleryController.ensureCategoryDirectory();

        AssetGalleryView view = viewBuilder.build(category, selectionModel::clear);
        view.uploadButton().setOnAction(e -> galleryController.handleUpload(this::loadImages));
        galleryPane = view.galleryPane();

        VBox.setVgrow(this, Priority.ALWAYS);
        getChildren().addAll(view.controls(), view.scrollPane());

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
        loadingCoordinator.load(
                galleryController,
                galleryPane,
                selectionModel::clear,
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

            AssetCard card = cardFactory.create(
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












