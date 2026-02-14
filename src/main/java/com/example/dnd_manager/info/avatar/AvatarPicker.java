package com.example.dnd_manager.info.avatar;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.text.dto.AvatarData;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Objects;

public class AvatarPicker extends VBox {

    private static final String DEFAULT_AVATAR = "/com/example/dnd_manager/icon/images.png";
    private final ImageView imageView = new ImageView();
    private final double AVATAR_SIZE = 220;

    public AvatarPicker() {
        this(null);
    }

    public AvatarPicker(Character character) {
        setSpacing(12);
        setAlignment(Pos.TOP_CENTER);

        StackPane imageContainer = new StackPane();

        // Рамка вокруг картинки в твоем стиле
        imageContainer.setStyle("""
        -fx-border-color: #4a4a4a;
        -fx-border-width: 2;
        -fx-border-radius: 10;
        -fx-background-radius: 10;
        -fx-padding: 3;
    """);

        imageView.setFitWidth(AVATAR_SIZE);
        imageView.setFitHeight(AVATAR_SIZE * 1.7);
        Rectangle clip = new Rectangle(AVATAR_SIZE, AVATAR_SIZE * 1.7);
        clip.setArcWidth(15); clip.setArcHeight(15);
        imageView.setClip(clip);

        imageContainer.getChildren().add(imageView);

        // Кнопка загрузки - теперь она выглядит как "шильдик" снизу
        Button uploadBtn = new Button("UPLOAD PORTRAIT");
        uploadBtn.setStyle("""
        -fx-background-color: #333;
        -fx-text-fill: #aaa;
        -fx-font-family: 'Cinzel', serif;
        -fx-font-size: 10px;
        -fx-border-color: #4a4a4a;
        -fx-border-radius: 5;
        -fx-cursor: hand;
        -fx-padding: 5 15;
    """);
        uploadBtn.setOnMouseEntered(e -> uploadBtn.setStyle(uploadBtn.getStyle() + "-fx-text-fill: #FFC107; -fx-border-color: #FFC107;"));
        uploadBtn.setOnMouseExited(e -> uploadBtn.setStyle(uploadBtn.getStyle().replace("-fx-text-fill: #FFC107; -fx-border-color: #FFC107;", "")));
        uploadBtn.setOnAction(event -> chooseImage());

        getChildren().addAll(imageContainer, uploadBtn);

        initImage(character == null ? null : CharacterAssetResolver.resolve(character.getName(), character.getAvatarImage()));
    }

    private void initImage(String url) {
        String finalUrl = (url == null)
                ? Objects.requireNonNull(getClass().getResource(DEFAULT_AVATAR)).toExternalForm()
                : url;
        imageView.setImage(new Image(finalUrl));
    }

    private void chooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            imageView.setImage(new Image(file.toURI().toString()));
        }
    }

    public AvatarData getData() {
        return new AvatarData(imageView.getImage().getUrl());
    }
}