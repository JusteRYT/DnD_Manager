    package com.example.dnd_manager.theme;

    import com.example.dnd_manager.assets.AssetCategory;
    import com.example.dnd_manager.assets.AssetGalleryTab;
    import com.example.dnd_manager.assets.logic.AssetDnDManager;
    import javafx.scene.control.Label;
    import javafx.scene.control.Tab;
    import javafx.stage.Stage;
    import lombok.Getter;

    import java.nio.file.Path;

    public class AppCustomTab extends Tab {

        @Getter
        private final AssetGalleryTab galleryTab;

        public AppCustomTab(AssetCategory category, Path rootPath, Stage stage, AssetDnDManager dndManager) {
            Label titleLabel = new Label(category.getDisplayName());
            setGraphic(titleLabel);
            setClosable(false);

            Path categoryPath = rootPath.resolve(category.getFolderName());
            this.galleryTab = new AssetGalleryTab(category, rootPath, stage, dndManager);
            setContent(galleryTab);
            dndManager.setupTarget(titleLabel, categoryPath, galleryTab::loadImages);
        }
    }