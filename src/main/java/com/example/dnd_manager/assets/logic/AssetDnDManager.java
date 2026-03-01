package com.example.dnd_manager.assets.logic;

import javafx.scene.Node;
import javafx.scene.input.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssetDnDManager {
    private static final Logger log = LoggerFactory.getLogger(AssetDnDManager.class);
    // Используем спец. формат для списка путей
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
            if (event.getDragboard().hasContent(ASSET_LIST_FORMAT)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        targetNode.setOnDragEntered(event -> {
            if (event.getDragboard().hasContent(ASSET_LIST_FORMAT)) {
                targetNode.setStyle("-fx-scale-x: 1.1; -fx-scale-y: 1.1; -fx-text-fill: #ffcc00;");
            }
        });

        targetNode.setOnDragExited(event -> targetNode.setStyle(""));

        targetNode.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(ASSET_LIST_FORMAT)) {
                String[] pathsAsStrings = ((String) db.getContent(ASSET_LIST_FORMAT)).split(";");

                for (String pathStr : pathsAsStrings) {
                    Path sourcePath = Paths.get(pathStr);
                    Path targetPath = targetDirectory.resolve(sourcePath.getFileName());

                    try {
                        if (!sourcePath.getParent().equals(targetDirectory)) {
                            Files.move(sourcePath, targetPath);
                            success = true;
                        }
                    } catch (IOException e) {
                        log.error("Failed to move {}", sourcePath, e);
                    }
                }

                if (success) {
                    onTargetRefresh.run();
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
}