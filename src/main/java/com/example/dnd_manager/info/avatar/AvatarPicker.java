package com.example.dnd_manager.info.avatar;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.text.dto.AvatarData;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Objects;

/**
 * Component for selecting and editing character avatar.
 * <p>
 * Supports CREATE and EDIT modes.
 */
public class AvatarPicker extends VBox {

    private static final String DEFAULT_AVATAR =
            "/com/example/dnd_manager/icon/images.png";

    private final ImageView imageView = new ImageView();

    /**
     * Creates avatar picker in CREATE mode.
     */
    public AvatarPicker() {
        this(null);
    }

    /**
     * Creates avatar picker with given mode and initial avatar.
     *
     * @param character character
     */
    public AvatarPicker(Character character) {
        if (Objects.isNull(character)) {
            initImage();
        } else {
            initImage(character.getAvatarImage(), character.getName());
        }
        initLayout();
    }

    /**
     * Initializes avatar image depending on mode.
     */
    private void initImage(String initialImageUrl, String name) {
        imageView.setFitWidth(220);
        imageView.setFitHeight(220);
        imageView.setPreserveRatio(true);

        imageView.setImage(new Image(CharacterAssetResolver.resolve(name, initialImageUrl)));
    }

    private void initImage() {
        imageView.setFitWidth(220);
        imageView.setFitHeight(220);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(
                Objects.requireNonNull(getClass().getResource(DEFAULT_AVATAR)).toExternalForm()
        ));

    }

    /**
     * Initializes UI layout.
     */
    private void initLayout() {
        Button uploadButton = AppButtonFactory.customButton("Upload photo", 120);
        uploadButton.setOnAction(event -> chooseImage());

        setSpacing(10);
        setPadding(new Insets(10, 0, 0, 0));
        setAlignment(Pos.CENTER);
        getChildren().addAll(imageView, uploadButton);
    }

    /**
     * Opens file chooser to select avatar image.
     */
    private void chooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = chooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            imageView.setImage(new Image(file.toURI().toString()));
        }
    }

    /**
     * Returns avatar data.
     *
     * @return avatar data DTO
     */
    public AvatarData getData() {
        return new AvatarData(imageView.getImage().getUrl());
    }
}