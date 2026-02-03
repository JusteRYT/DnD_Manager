package com.example.dnd_manager.info.avatar;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Component for selecting character avatar.
 */
public class AvatarPicker extends VBox {

    private final ImageView imageView;

    public AvatarPicker() {
        this.imageView = new ImageView("com/example/dnd_manager/icon/images.png");
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        Button uploadButton = new Button("Upload avatar");
        uploadButton.setOnAction(event -> chooseImage());

        setSpacing(10);
        getChildren().addAll(imageView, uploadButton);
    }

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
     * @return selected avatar image
     */
    public String getImage() {
        return imageView.getImage().getUrl();
    }
}
