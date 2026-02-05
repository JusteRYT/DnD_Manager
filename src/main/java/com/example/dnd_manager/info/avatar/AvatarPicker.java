package com.example.dnd_manager.info.avatar;

import com.example.dnd_manager.theme.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
        imageView.setFitWidth(220);
        imageView.setFitHeight(220);
        imageView.setPreserveRatio(true);

        Button uploadButton = AppButtonFactory.customButton("Upload photo", 120);
        uploadButton.setOnAction(event -> chooseImage());

        setSpacing(10);
        setPadding(new Insets(10, 0, 0, 0));
        setAlignment(Pos.CENTER);
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
