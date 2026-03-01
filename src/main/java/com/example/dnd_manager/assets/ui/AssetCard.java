package com.example.dnd_manager.assets.ui;

import com.example.dnd_manager.assets.logic.AssetActionHandler;
import com.example.dnd_manager.assets.logic.AssetDnDManager;
import com.example.dnd_manager.assets.logic.AssetSelectionModel;
import com.example.dnd_manager.theme.AppContextMenu;
import com.example.dnd_manager.theme.AppTheme;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.nio.file.Path;

public class AssetCard extends VBox {
    private final Path filePath;
    private final AssetSelectionModel selectionModel;

    public AssetCard(Path filePath, Image img, AssetSelectionModel selectionModel,
                     AssetActionHandler actionHandler, AssetDnDManager dndManager, Runnable refreshGallery) {
        {
            this.filePath = filePath;
            this.selectionModel = selectionModel;

            setAlignment(Pos.CENTER);
            setPadding(new Insets(10));
            setPrefSize(130, 150);
            updateStyle();

            ImageView view = new ImageView(img);
            view.setFitWidth(90);
            view.setFitHeight(90);
            view.setPreserveRatio(true);

            Label label = new Label(filePath.getFileName().toString());
            label.setStyle("-fx-text-fill: white; -fx-font-size: 11px;");
            label.setTextOverrun(OverrunStyle.CENTER_ELLIPSIS);

            getChildren().addAll(view, label);

            // Инициализация DND
            dndManager.setupSource(this, filePath, selectionModel, refreshGallery);

            // Клики
            setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    if (e.isControlDown()) selectionModel.toggle(filePath);
                    else selectionModel.clearAndSelect(filePath);
                } else if (e.getButton() == MouseButton.SECONDARY) {
                    if (!selectionModel.isSelected(filePath)) selectionModel.clearAndSelect(filePath);
                    showMenu(e, actionHandler);
                }
                updateStyle();
            });

            // Слушаем изменения выделения, чтобы обновлять стиль снаружи
            selectionModel.getSelectedPaths().addListener((SetChangeListener<Path>) c -> updateStyle());
        }
    }

    private void updateStyle() {
        boolean selected = selectionModel.isSelected(filePath);
        setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-width: 2; " +
                "-fx-background-color: " + (selected ? "#404040;" : "#2b2b2b;") +
                "-fx-border-color: " + (selected ? AppTheme.TEXT_ACCENT : "transparent") + ";");
    }

    private void showMenu(javafx.scene.input.MouseEvent e, AssetActionHandler handler) {
        AppContextMenu menu = new AppContextMenu();

        menu.addActionItem("Rename", () -> handler.rename(filePath));
        menu.addDeleteItem("Delete", () -> handler.delete(selectionModel.getSelectedPaths()));

        menu.show(this, e.getScreenX(), e.getScreenY());
    }
}