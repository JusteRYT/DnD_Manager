package com.example.dnd_manager.info.buff_debuff;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

/**
 * UI component representing a buff or debuff entry.
 * Includes an icon at the start and a remove button (cross) at the end.
 */
@Getter
public class BuffEditorRow extends HBox {

    private final Buff buff;

    public BuffEditorRow(Buff buff, Runnable onRemove, Character character) {
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
        iconView.setImage(chooseIcon(buff, character));

        // --- Info box ---
        VBox infoBox = new VBox(2);
        Label title = new Label(buff.name() + " (" + buff.type() + ")");
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #c89b3c;");

        Label description = new Label(buff.description());
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: #e6e6e6;");

        infoBox.getChildren().addAll(title, description);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Button removeButton = AppButtonFactory.customButton("✕", 35, AppTheme.BUTTON_DANGER, AppTheme.BUTTON_DANGER_HOVER);
        removeButton.setOnAction(e -> onRemove.run());
        removeButton.setFocusTraversable(false);
        getChildren().addAll(iconView, infoBox, removeButton);
    }

    private Image chooseIcon(Buff buff, Character character) {
        Image image = null;
        if (buff.iconPath() != null && !buff.iconPath().isEmpty() && character != null) {
            image = new Image(CharacterAssetResolver.resolve(character.getName(), buff.iconPath()));
        } else {
            if (buff.iconPath() != null && !buff.iconPath().isEmpty()) {
                image = new Image("file:" + buff.iconPath());
            }
        }

        return image;
    }
}