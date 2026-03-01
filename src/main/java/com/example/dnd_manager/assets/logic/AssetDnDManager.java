package com.example.dnd_manager.assets.logic;

import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

public class AssetDnDManager {
    private static final Logger log = LoggerFactory.getLogger(AssetDnDManager.class);
    public static final DataFormat ASSET_LIST_FORMAT = new DataFormat("application/x-asset-list");

    public void setupSource(Node node, Path filePath, AssetSelectionModel selectionModel, Runnable onSourceRefresh) {
        node.setOnDragDetected(event -> {
            Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            // Если тянем выделенный элемент, берем всё выделение. Иначе — только этот файл.
            List<String> pathsToMove = selectionModel.isSelected(filePath)
                    ? selectionModel.getSelectedPaths().stream().map(Path::toString).collect(Collectors.toList())
                    : List.of(filePath.toString());

            content.put(ASSET_LIST_FORMAT, String.join(";", pathsToMove));
            db.setContent(content);
            event.consume();
        });

        // САМОЕ ВАЖНОЕ: После того как дроп завершен, исходная папка должна обновиться
        node.setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                log.debug("Drag done, refreshing source gallery");
                onSourceRefresh.run();
            }
            event.consume();
        });
    }

    public void setupTarget(Node targetNode, Path targetDirectory, Runnable onTargetRefresh) {
        targetNode.setOnDragOver(event -> {
            if (event.getDragboard().hasContent(ASSET_LIST_FORMAT) || event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        targetNode.setOnDragEntered(event -> {
            // Подсветка при наведении (эффект HUD)
            targetNode.setStyle("-fx-scale-x: 1.1; -fx-scale-y: 1.1; -fx-text-fill: #FFC107; -fx-cursor: hand;");
        });

        targetNode.setOnDragExited(event -> targetNode.setStyle(""));

        targetNode.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(ASSET_LIST_FORMAT)) {
                String[] pathsAsStrings = ((String) db.getContent(ASSET_LIST_FORMAT)).split(";");
                for (String pathStr : pathsAsStrings) {
                    try {
                        Path source = Paths.get(pathStr);
                        Path target = targetDirectory.resolve(source.getFileName());

                        if (!source.getParent().equals(targetDirectory)) {
                            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                            success = true;
                        }
                    } catch (IOException e) { log.error("Move failed", e); }
                }
            }
            else if (db.hasFiles()) {
                for (java.io.File file : db.getFiles()) {
                    try {
                        Files.copy(file.toPath(), targetDirectory.resolve(file.getName()),
                                StandardCopyOption.REPLACE_EXISTING);
                        success = true;
                    } catch (IOException e) { log.error("Copy failed", e); }
                }
            }

            if (success) onTargetRefresh.run();
            event.setDropCompleted(success);
            event.consume();
        });
    }
}