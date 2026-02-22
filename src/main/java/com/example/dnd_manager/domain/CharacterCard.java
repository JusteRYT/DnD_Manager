package com.example.dnd_manager.domain;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.function.Consumer;

public class CharacterCard extends VBox {

    private static final double CARD_WIDTH = 250;
    private static final double IMAGE_HEIGHT = 300; // Вертикальное соотношение

    public CharacterCard(Character character, Consumer<Character> onSelected, boolean isEdit, Runnable onDelete) {
        setPrefWidth(CARD_WIDTH);
        setMinWidth(CARD_WIDTH);
        setMaxWidth(CARD_WIDTH);
        setSpacing(10);
        setPadding(new Insets(12));
        setAlignment(Pos.TOP_CENTER);
        setCursor(Cursor.HAND);

        // --- Эффекты ---
        DropShadow shadow = new DropShadow(15, Color.BLACK);
        setEffect(shadow);

        // Динамические стили
        String baseStyle = String.format("-fx-background-color: %s; -fx-background-radius: 15; -fx-border-color: %s; -fx-border-width: 2; -fx-border-radius: 13;",
                AppTheme.BACKGROUND_SECONDARY, AppTheme.BORDER_MUTED);

        String hoverStyle = String.format("-fx-background-color: #323232; -fx-background-radius: 15; -fx-border-color: %s; -fx-border-width: 2; -fx-border-radius: 13; -fx-effect: dropshadow(three-pass-box, %s, 15, 0.5, 0, 0);",
                AppTheme.BORDER_ACCENT, AppTheme.BORDER_ACCENT);

        setStyle(baseStyle);

        // --- Аватар (Вертикальный) ---
        ImageView avatar = new ImageView();
        try {
            Image img = new Image(CharacterAssetResolver.resolve(character.getName(), character.getAvatarImage()), true);
            avatar.setImage(img);
        } catch (Exception ignored) {

        }

        avatar.setFitWidth(CARD_WIDTH - 24);
        avatar.setFitHeight(IMAGE_HEIGHT);
        avatar.setPreserveRatio(false); // Растягиваем на всю высоту

        // Скругление только верхних углов у картинки
        Rectangle clip = new Rectangle(CARD_WIDTH - 24, IMAGE_HEIGHT);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        avatar.setClip(clip);

        // --- Имя (Красивое оформление) ---
        Label nameLabel = new Label(character.getName().toUpperCase());
        nameLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-alignment: center;", AppTheme.TEXT_ACCENT));
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(CARD_WIDTH - 30);
        nameLabel.setAlignment(Pos.CENTER);

        // --- Распорка (Spacer) ---
        // Она забирает всё свободное место, толкая кнопку удаления вниз
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(avatar, nameLabel, spacer);

        // --- Кнопка удаления ---
        if (isEdit) {
            Button deleteBtn = AppButtonFactory.customButton(
                    I18n.t("button.delete"),
                    0,
                    AppTheme.BUTTON_DANGER,
                    AppTheme.BUTTON_DANGER_HOVER
            );
            deleteBtn.setMaxWidth(Double.MAX_VALUE);
            deleteBtn.setOnAction(e -> {
                e.consume();
                onDelete.run();
            });
            getChildren().add(deleteBtn);
        }

        // --- Логика наведения ---
        setOnMouseEntered(e -> setStyle(hoverStyle));
        setOnMouseExited(e -> setStyle(baseStyle));
        setOnMouseClicked(e -> onSelected.accept(character));
    }
}
