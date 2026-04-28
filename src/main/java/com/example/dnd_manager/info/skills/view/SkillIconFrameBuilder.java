package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.info.skills.model.Skill;
import com.example.dnd_manager.infrastructure.assets.CharacterAssetResolver;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class SkillIconFrameBuilder {

    private final SkillCardStyleProvider styleProvider;
    private final SkillSourceBadgeViewFactory sourceBadgeViewFactory;

    public SkillIconFrameBuilder(
            SkillCardStyleProvider styleProvider,
            SkillSourceBadgeViewFactory sourceBadgeViewFactory
    ) {
        this.styleProvider = styleProvider;
        this.sourceBadgeViewFactory = sourceBadgeViewFactory;
    }

    public StackPane build(
            Skill skill,
            Character character,
            InventoryItem sourceItem,
            int iconSize,
            int imageSize
    ) {
        ImageView icon = new ImageView();
        icon.setImage(CharacterAssetResolver.getImage(character, skill.iconPath(), imageSize, imageSize));
        icon.setFitWidth(iconSize);
        icon.setFitHeight(iconSize);
        icon.setPreserveRatio(true);
        icon.setSmooth(true);
        icon.setCache(true);

        StackPane iconFrame = new StackPane(icon);
        iconFrame.setMaxSize(iconSize + 4, iconSize + 4);
        iconFrame.setStyle(styleProvider.iconFrameStyle());
        iconFrame.setEffect(createFrameGlow());

        Label sourceBadge = sourceBadgeViewFactory.create(sourceItem);
        StackPane.setAlignment(sourceBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(sourceBadge, new Insets(-8, -8, 0, 0));
        iconFrame.getChildren().add(sourceBadge);
        return iconFrame;
    }

    private DropShadow createFrameGlow() {
        DropShadow frameGlow = new DropShadow();
        frameGlow.setBlurType(BlurType.THREE_PASS_BOX);
        frameGlow.setColor(Color.web("#c89b3c", 0.7));
        frameGlow.setRadius(12);
        frameGlow.setSpread(0.1);
        return frameGlow;
    }
}












