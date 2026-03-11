package com.example.dnd_manager.info.avatar;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.text.dto.AvatarData;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;

public class AvatarPicker extends VBox {

    private static final String DEFAULT_AVATAR = "/com/example/dnd_manager/icon/user.png";
    private final ImageView imageView = new ImageView();
    private final double AVATAR_SIZE = 220;

    private String currentPath;
    private final Character character;

    public AvatarPicker() {
        this(null);
    }

    public AvatarPicker(Character character) {
        this.character = character;

        // 1. Устанавливаем начальный путь
        this.currentPath = (character != null && character.getAvatarImage() != null)
                ? character.getAvatarImage()
                : DEFAULT_AVATAR;

        setSpacing(12);
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(25, 0, 0, 0));

        // Контейнер и ImageView
        StackPane imageContainer = new StackPane();
        imageContainer.setStyle("""
                    -fx-border-color: #4a4a4a;
                    -fx-border-width: 2;
                    -fx-border-radius: 10;
                    -fx-background-radius: 10;
                    -fx-padding: 3;
                """);

        imageView.setFitWidth(AVATAR_SIZE);
        imageView.setFitHeight(AVATAR_SIZE * 1.2);

        Rectangle clip = new Rectangle(AVATAR_SIZE, AVATAR_SIZE * 1.2);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        imageView.setClip(clip);
        imageContainer.getChildren().add(imageView);

        // Кнопки
        Button uploadBtn = createStyledButton(I18n.t("button.upload"));
        uploadBtn.setOnAction(event -> chooseFromFileSystem());

        Button galleryBtn = AppButtonFactory.assetPickerButton();
        AppButtonFactory.attachAssetPicker(galleryBtn, this::updateAvatarState);

        HBox controls = new HBox(10, uploadBtn, galleryBtn);
        controls.setAlignment(Pos.CENTER);

        getChildren().addAll(imageContainer, controls);

        // 2. Отрисовываем картинку ОДНИМ методом
        refreshUI();
    }

    private void updateAvatarState(String newPath) {
        this.currentPath = (newPath == null || newPath.isBlank()) ? DEFAULT_AVATAR : newPath;
        refreshUI();
    }

    private void refreshUI() {
        // CharacterAssetResolver должен содержать метод getAvatarImage,
        // который мы обсуждали в прошлом сообщении.
        Image img = CharacterAssetResolver.getAvatarImage(character, currentPath, AVATAR_SIZE, AVATAR_SIZE * 1.2);
        imageView.setImage(img);
    }

    private void chooseFromFileSystem() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp"));
        File file = chooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            updateAvatarState(file.getAbsolutePath());
        }
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        String accent = AppTheme.TEXT_ACCENT;
        String style = """
        -fx-background-color: #2f2f2f;
        -fx-text-fill: #c89b3c;
        -fx-font-family: 'Cinzel';
        -fx-font-size: 13px;
        -fx-font-weight: bold;
        -fx-background-radius: 6;
        -fx-border-radius: 6;
        -fx-border-color: #444;
        -fx-border-width: 1;
        -fx-padding: 6 14;
        -fx-cursor: hand;
        """;
        btn.setStyle(style);
        btn.setOnMouseEntered(e -> btn.setStyle(style + "-fx-background-color: #353535; -fx-text-fill: " + accent + "; -fx-border-color: " + accent + ";"));
        btn.setOnMouseExited(e -> btn.setStyle(style));
        return btn;
    }

    public AvatarData getData() {
        return new AvatarData(currentPath);
    }
}