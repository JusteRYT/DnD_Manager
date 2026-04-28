package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.inventory.model.InventoryItem;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SkillSourceBadgeViewFactory {

    private static final double BADGE_SIZE = 22;

    private final SkillCardStyleProvider styleProvider;
    private final SkillSourceBadgeViewModelFactory viewModelFactory;

    public SkillSourceBadgeViewFactory(
            SkillCardStyleProvider styleProvider,
            SkillSourceBadgeViewModelFactory viewModelFactory
    ) {
        this.styleProvider = styleProvider;
        this.viewModelFactory = viewModelFactory;
    }

    public Label create(InventoryItem sourceItem) {
        SkillSourceBadgeViewModel viewModel = viewModelFactory.create(sourceItem);
        Label label = new Label(viewModel.iconText());
        label.setMinSize(BADGE_SIZE, BADGE_SIZE);
        label.setMaxSize(BADGE_SIZE, BADGE_SIZE);
        label.setPrefSize(BADGE_SIZE, BADGE_SIZE);
        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setStyle(styleProvider.sourceBadgeStyle(viewModel.backgroundColor(), viewModel.textColor()));

        Tooltip tooltip = new Tooltip(viewModel.tooltipText());
        tooltip.setShowDelay(Duration.millis(100));
        label.setTooltip(tooltip);
        label.setEffect(createGlow(sourceItem, viewModel));
        return label;
    }

    private DropShadow createGlow(InventoryItem sourceItem, SkillSourceBadgeViewModel viewModel) {
        DropShadow glow = new DropShadow();
        glow.setBlurType(BlurType.THREE_PASS_BOX);
        glow.setRadius(10);
        glow.setSpread(0.3);
        glow.setColor(sourceItem != null
                ? Color.web(viewModel.backgroundColor(), 0.8)
                : Color.web("#c89b3c", 0.6));
        return glow;
    }
}












