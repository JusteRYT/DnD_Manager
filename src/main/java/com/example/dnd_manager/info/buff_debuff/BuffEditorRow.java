package com.example.dnd_manager.info.buff_debuff;

import com.example.dnd_manager.theme.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.File;

/**
 * UI component representing a buff or debuff entry.
 * Includes an icon at the start and a remove button (cross) at the end.
 */
@Getter
public class BuffEditorRow extends HBox {

    private final Buff buff;

    public BuffEditorRow(Buff buff, Runnable onRemove) {
        this.buff = buff;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        setStyle("""
            -fx-background-color: #2b2b2b;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            -fx-border-color: #444444;
            -fx-padding: 6;
        """);

        // --- Icon ---
        ImageView iconView = new ImageView();
        iconView.setFitWidth(30);
        iconView.setFitHeight(30);
        iconView.setPreserveRatio(true);

        if (buff.iconPath() != null && !buff.iconPath().isEmpty()) {
            File iconFile = new File(buff.iconPath());
            if (iconFile.exists()) {
                iconView.setImage(new Image(iconFile.toURI().toString()));
            }
        }

        // --- Info box ---
        VBox infoBox = new VBox(2);
        Label title = new Label(buff.name() + " (" + buff.type() + ")");
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #c89b3c;");

        Label description = new Label(buff.description());
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: #e6e6e6;");

        infoBox.getChildren().addAll(title, description);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Button removeButton = AppButtonFactory.customButton("✕", 30);
        removeButton.setOnAction(e -> onRemove.run());

        getChildren().addAll(iconView, infoBox, removeButton);
    }
}