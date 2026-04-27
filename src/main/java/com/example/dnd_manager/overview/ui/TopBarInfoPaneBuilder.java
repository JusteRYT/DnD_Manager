package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Objects;

public class TopBarInfoPaneBuilder {

    private final TopBarInfoStyleProvider styleProvider;

    public TopBarInfoPaneBuilder() {
        this(new TopBarInfoStyleProvider());
    }

    TopBarInfoPaneBuilder(TopBarInfoStyleProvider styleProvider) {
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
    }

    public TopBarInfoComponents build(Character character) {
        Image avatarImg = CharacterAssetResolver.getAvatarImage(character, character.getAvatarImage(), 400, 600);
        StackPane avatarContainer = AvatarClipboardPane.create(avatarImg);
        avatarContainer.setPrefSize(160, 220);
        avatarContainer.setMinSize(160, 220);
        avatarContainer.setMaxWidth(220);

        Label nameLabel = new Label(character.getName());
        nameLabel.setStyle(styleProvider.nameStyle());

        Label levelText = new Label(I18n.t("topBar.level"));
        levelText.setStyle(styleProvider.levelTextStyle());

        Label levelValue = new Label(String.valueOf(character.getLevel()));
        levelValue.setStyle(styleProvider.levelValueStyle());

        HBox levelBox = new HBox(4, levelText, levelValue);
        levelBox.setAlignment(Pos.CENTER);
        levelBox.setPadding(new Insets(4, 8, 4, 8));
        levelBox.setStyle(styleProvider.levelBoxStyle());

        HBox nameLevelBox = new HBox(10, nameLabel, levelBox);
        nameLevelBox.setAlignment(Pos.CENTER_LEFT);

        Label metaLabel = new Label(character.getRace() + " • " + character.getCharacterClass());
        metaLabel.setStyle(styleProvider.metaLabelStyle());

        ImageView hpIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_heart.png")).toExternalForm()));
        hpIcon.setFitWidth(25);
        hpIcon.setFitHeight(25);
        Label hpLabel = new Label(String.valueOf(character.getCurrentHp()));
        hpLabel.setStyle(styleProvider.hpValueStyle());
        HBox hpBox = new HBox(6, hpIcon, hpLabel);
        hpBox.setAlignment(Pos.CENTER_LEFT);

        ImageView armorIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/com/example/dnd_manager/icon/icon_shield.png")).toExternalForm()));
        armorIcon.setFitWidth(25);
        armorIcon.setFitHeight(25);
        Label armorLabel = new Label(String.valueOf(character.getArmor()));
        armorLabel.setStyle(styleProvider.armorValueStyle());
        HBox armorBox = new HBox(6, armorIcon, armorLabel);
        armorBox.setAlignment(Pos.CENTER_LEFT);

        HBox statsBox = new HBox(12, hpBox, armorBox);
        statsBox.setAlignment(Pos.CENTER_LEFT);
        statsBox.setPadding(new Insets(8, 0, 0, 0));
        HBox.setHgrow(avatarContainer, Priority.SOMETIMES);

        ActiveEffectsPane activeEffectsPane = new ActiveEffectsPane(character);
        VBox infoBox = new VBox(8, nameLevelBox, metaLabel, statsBox, activeEffectsPane);
        infoBox.setAlignment(Pos.TOP_LEFT);
        infoBox.setPadding(new Insets(10, 0, 0, 0));

        HBox leftBox = new HBox(20, avatarContainer, infoBox);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setPadding(new Insets(15, 25, 15, 20));
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        leftBox.setStyle(styleProvider.leftBoxStyle());

        return new TopBarInfoComponents(leftBox, hpLabel, armorLabel, levelValue, activeEffectsPane);
    }
}
