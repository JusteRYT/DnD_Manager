package com.example.dnd_manager.info.avatar;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.text.dto.AvatarData;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.infrastructure.assets.CharacterAssetResolver;
import com.example.dnd_manager.theme.button.AppButtonFactory;
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
    private final AvatarPickerStyleProvider styleProvider = new AvatarPickerStyleProvider();

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
        imageContainer.setStyle(styleProvider.frameStyle());

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
        galleryBtn.setPrefWidth(120);
        galleryBtn.setStyle(styleProvider.actionButtonStyle(false));
        galleryBtn.setOnMouseEntered(e -> galleryBtn.setStyle(styleProvider.actionButtonStyle(true)));
        galleryBtn.setOnMouseExited(e -> galleryBtn.setStyle(styleProvider.actionButtonStyle(false)));
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
        btn.setStyle(styleProvider.actionButtonStyle(false));
        btn.setOnMouseEntered(e -> btn.setStyle(styleProvider.actionButtonStyle(true)));
        btn.setOnMouseExited(e -> btn.setStyle(styleProvider.actionButtonStyle(false)));
        return btn;
    }

    public AvatarData getData() {
        return new AvatarData(currentPath);
    }
}











