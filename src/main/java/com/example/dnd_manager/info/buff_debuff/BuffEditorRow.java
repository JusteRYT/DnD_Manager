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

import java.util.Objects;

import static com.example.dnd_manager.info.skills.SkillCard.getImage;

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

        String baseStyle = """
                    -fx-background-color: linear-gradient(to right, #2b2b2b, #212121);
                    -fx-background-radius: 6;
                    -fx-border-radius: 6;
                    -fx-border-color: #3a3a3a;
                    -fx-padding: 8;
                """;

        String hoverStyle = baseStyle + """
                    -fx-border-color: #c89b3c;
                    -fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.1), 10, 0, 0, 0);
                """;

        setStyle(baseStyle);
        this.setOnMouseEntered(e -> setStyle(hoverStyle));
        this.setOnMouseExited(e -> setStyle(baseStyle));

        // --- Icon ---
        ImageView iconView = new ImageView();
        iconView.setFitWidth(30);
        iconView.setFitHeight(30);
        iconView.setPreserveRatio(true);
        if (character != null) {
            iconView.setImage(chooseIcon(buff, character));
        } else {
            iconView.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/no_image.png")).toExternalForm()));
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

        Button removeButton = AppButtonFactory.deleteButton(35);
        removeButton.setOnAction(e -> onRemove.run());
        removeButton.setFocusTraversable(false);
        getChildren().addAll(iconView, infoBox, removeButton);
    }

    private Image chooseIcon(Buff buff, Character character) {
        return getImage(character, buff.iconPath());
    }
}