package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.domain.Character;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class FamiliarCardBuilder {

    private final FamiliarCardStyleProvider styleProvider;
    private final FamiliarAvatarResolver avatarResolver;

    public FamiliarCardBuilder() {
        this(new FamiliarCardStyleProvider(), new CharacterAssetFamiliarAvatarResolver());
    }

    FamiliarCardBuilder(FamiliarCardStyleProvider styleProvider, FamiliarAvatarResolver avatarResolver) {
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
        this.avatarResolver = Objects.requireNonNull(avatarResolver, "avatarResolver must not be null");
    }

    public HBox build(
            String ownerName,
            Character familiar,
            FamiliarCardViewModel viewModel,
            Runnable onOpenDetails
    ) {
        HBox card = new HBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(8));
        card.setStyle(styleProvider.cardIdleStyle());
        card.setOnMouseEntered(e -> card.setStyle(styleProvider.cardHoverStyle()));
        card.setOnMouseExited(e -> card.setStyle(styleProvider.cardIdleStyle()));
        card.setOnMouseClicked(e -> onOpenDetails.run());

        ImageView avatar = new ImageView(avatarResolver.resolve(ownerName, familiar.getAvatarImage()));
        avatar.setFitWidth(40);
        avatar.setFitHeight(40);
        avatar.setClip(new Circle(20, 20, 20));

        Label nameLabel = new Label(viewModel.name());
        nameLabel.setStyle(styleProvider.nameLabelStyle());

        Label raceClassLabel = new Label(viewModel.raceClass());
        raceClassLabel.setStyle(styleProvider.raceClassLabelStyle());

        VBox infoBox = new VBox(2, nameLabel, raceClassLabel);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        VBox statsBox = new VBox(2);
        statsBox.setAlignment(Pos.CENTER_RIGHT);
        Label hpLabel = new Label(viewModel.hpText());
        hpLabel.setStyle(styleProvider.hpLabelStyle());
        Label acLabel = new Label(viewModel.acText());
        acLabel.setStyle(styleProvider.acLabelStyle());
        statsBox.getChildren().addAll(hpLabel, acLabel);

        card.getChildren().addAll(avatar, infoBox, statsBox);
        return card;
    }
}

