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
import java.util.Objects;

public class AvatarPicker extends VBox {

    private static final String DEFAULT_AVATAR = "/com/example/dnd_manager/icon/user.png";
    private final ImageView imageView = new ImageView();
    private final double AVATAR_SIZE = 220;
    private String currentPath;

    public AvatarPicker() {
        this(null);
    }

    public AvatarPicker(Character character) {
        setSpacing(12);
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(25, 0, 0, 0));
        this.currentPath = (character != null) ? character.getAvatarImage() : null;

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
        imageView.setFitHeight(AVATAR_SIZE * 1.2);

        Rectangle clip = new Rectangle(AVATAR_SIZE, AVATAR_SIZE * 1.2);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        imageView.setClip(clip);

        imageContainer.getChildren().add(imageView);

        Button uploadBtn = createStyledButton(I18n.t("button.upload"));
        uploadBtn.setOnAction(event -> chooseFromFileSystem());

        Button galleryBtn = AppButtonFactory.assetPickerButton();
        AppButtonFactory.attachAssetPicker(galleryBtn, path -> {
            this.currentPath = path;
            imageView.setImage(new Image(new File(path).toURI().toString()));
        });

        HBox controls = new HBox(10, uploadBtn, galleryBtn);
        controls.setAlignment(Pos.CENTER);

        getChildren().addAll(imageContainer, controls);

        initImage(character == null ? null : CharacterAssetResolver.resolve(character.getName(), currentPath));
    }

    private void initImage(String url) {
        String finalUrl = (url == null)
                ? Objects.requireNonNull(getClass().getResource(DEFAULT_AVATAR)).toExternalForm()
                : url;
        imageView.setImage(new Image(finalUrl));
    }

    private void chooseFromFileSystem() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.webp"));
        File file = chooser.showOpenDialog(getScene().getWindow());
        if (file != null) {
            this.currentPath = file.getAbsolutePath();
            imageView.setImage(new Image(file.toURI().toString()));
        }
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);

        String accent = AppTheme.TEXT_ACCENT;

        String baseStyle = """
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

        String hoverStyle = """
        -fx-background-color: #353535;
        -fx-text-fill: %s;
        -fx-font-family: 'Cinzel';
        -fx-font-size: 13px;
        -fx-font-weight: bold;
        -fx-background-radius: 6;
        -fx-border-radius: 6;
        -fx-border-color: %s;
        -fx-border-width: 1;
        -fx-padding: 6 14;
        -fx-cursor: hand;
        """.formatted(accent, accent);

        btn.setStyle(baseStyle);

        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));

        btn.setOnMousePressed(e -> btn.setTranslateY(1));
        btn.setOnMouseReleased(e -> btn.setTranslateY(0));

        return btn;
    }

    public AvatarData getData() {
        return new AvatarData(currentPath);
    }
}