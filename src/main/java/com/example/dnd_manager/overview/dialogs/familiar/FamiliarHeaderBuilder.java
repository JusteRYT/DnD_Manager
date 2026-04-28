package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.domain.Character;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class FamiliarHeaderBuilder {

    private final FamiliarAvatarImageResolver avatarImageResolver;
    private final FamiliarMetaTextFormatter metaTextFormatter;
    private final FamiliarHeaderStyleProvider styleProvider;

    public FamiliarHeaderBuilder() {
        this(
                new CharacterAssetFamiliarAvatarImageResolver(),
                new FamiliarMetaTextFormatter(),
                new FamiliarHeaderStyleProvider()
        );
    }

    FamiliarHeaderBuilder(
            FamiliarAvatarImageResolver avatarImageResolver,
            FamiliarMetaTextFormatter metaTextFormatter,
            FamiliarHeaderStyleProvider styleProvider
    ) {
        this.avatarImageResolver = Objects.requireNonNull(avatarImageResolver, "avatarImageResolver must not be null");
        this.metaTextFormatter = Objects.requireNonNull(metaTextFormatter, "metaTextFormatter must not be null");
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
    }

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
        return avatarImageResolver.resolve(familiar, owner);
    }

    private VBox createInfo(Character familiar) {
        VBox info = new VBox(2);

        Label name = new Label(familiar.getName());
        name.setStyle(styleProvider.nameStyle());

        String metaText = metaTextFormatter.format(familiar);
        Label meta = new Label(metaText);
        meta.setStyle(styleProvider.metaStyle());

        info.getChildren().addAll(name, meta);
        return info;
    }
}












