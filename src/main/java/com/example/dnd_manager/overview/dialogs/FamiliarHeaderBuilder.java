package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class FamiliarHeaderBuilder {

    private static final String FALLBACK_IMAGE = "/com/example/dnd_manager/icon/no_image.png";

    public HBox build(Character familiar, Character owner) {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        ImageView avatar = createAvatar(familiar, owner);
        VBox info = createInfo(familiar);

        header.getChildren().addAll(avatar, info);
        return header;
    }

    private ImageView createAvatar(Character familiar, Character owner) {
        ImageView avatar = new ImageView();
        avatar.setFitWidth(80);
        avatar.setFitHeight(80);
        avatar.setImage(resolveAvatar(familiar, owner));
        avatar.setClip(new Circle(40, 40, 40));
        return avatar;
    }

    private Image resolveAvatar(Character familiar, Character owner) {
        try {
            return new Image(CharacterAssetResolver.resolve(owner.getName(), familiar.getAvatarImage()));
        } catch (Exception ignored) {
            return new Image(getClass().getResource(FALLBACK_IMAGE).toExternalForm());
        }
    }

    private VBox createInfo(Character familiar) {
        VBox info = new VBox(2);

        Label name = new Label(familiar.getName());
        name.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #9c27b0;");

        String metaText = String.format(
                "%s • %s • %s %s",
                familiar.getRace(),
                familiar.getCharacterClass(),
                I18n.t("label.familiarsLvl"),
                familiar.getLevel()
        );
        Label meta = new Label(metaText);
        meta.setStyle("-fx-text-fill: #888;");

        info.getChildren().addAll(name, meta);
        return info;
    }
}
