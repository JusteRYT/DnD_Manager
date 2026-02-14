package com.example.dnd_manager.info.inventory;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class InventoryRow extends HBox {

    private final InventoryItem item;

    public InventoryRow(InventoryItem item, Runnable onDelete, Character character) {
        this.item = item;

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);

        // Базовый стиль с градиентом (как в BuffEditorRow)
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

        // Иконка
        ImageView iconView = new ImageView();
        iconView.setFitWidth(32);
        iconView.setFitHeight(32);
        iconView.setPreserveRatio(true);
        iconView.setImage(chooseIcon(item, character));
        iconView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 5, 0, 0, 0);");

        // Текстовый блок
        VBox textBox = new VBox(2);
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #c89b3c;");

        Label descLabel = new Label(item.getDescription());
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #e6e6e6; -fx-font-size: 12px;");

        textBox.getChildren().addAll(nameLabel, descLabel);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        // Кнопка удаления (используем твой AppButtonFactory)
        Button deleteButton = AppButtonFactory.deleteButton(35);
        deleteButton.setOnAction(e -> onDelete.run());
        deleteButton.setFocusTraversable(false);

        getChildren().addAll(iconView, textBox, deleteButton);
    }

    private Image chooseIcon(InventoryItem item, Character character) {
        if (item.getIconPath() != null && !item.getIconPath().isEmpty()) {
            try {
                if (character != null) {
                    return new Image(CharacterAssetResolver.resolve(character.getName(), item.getIconPath()));
                }
                return new Image("file:" + item.getIconPath());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}