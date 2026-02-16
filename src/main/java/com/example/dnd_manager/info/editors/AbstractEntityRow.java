package com.example.dnd_manager.info.editors;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.skills.SkillCard;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public abstract class AbstractEntityRow<T> extends HBox {

    protected final T item;
    protected final Character character;

    public AbstractEntityRow(T item, Runnable onRemove, Character character) {
        this.item = item;
        this.character = character;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);

        // --- Styles ---
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
        iconView.setFitWidth(32);
        iconView.setFitHeight(32);
        iconView.setPreserveRatio(true);
        iconView.setImage(resolveIcon(item, character));
        iconView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0, 0, 0);");

        // --- Info Box (Content) ---
        VBox infoBox = new VBox(2);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        fillContent(infoBox, item);

        // --- Delete Button ---
        Button removeButton = AppButtonFactory.deleteButton(35);
        removeButton.setOnAction(e -> onRemove.run());
        removeButton.setFocusTraversable(false);

        getChildren().addAll(iconView, infoBox, removeButton);
    }

    /**
     * Наследник должен наполнить VBox метками (Label)
     */
    protected abstract void fillContent(VBox container, T item);

    /**
     * Наследник должен вернуть путь к иконке
     */
    protected abstract String getIconPath(T item);

    // Общая логика загрузки картинки
    private Image resolveIcon(T item, Character character) {
        return SkillCard.getImage(character, getIconPath(item));
    }
}
