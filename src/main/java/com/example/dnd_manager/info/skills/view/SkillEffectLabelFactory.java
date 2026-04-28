package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.info.skills.model.SkillEffect;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class SkillEffectLabelFactory {

    private final SkillCardStyleProvider styleProvider;
    private final SkillEffectBadgeFactory effectBadgeFactory;

    public SkillEffectLabelFactory(SkillCardStyleProvider styleProvider, SkillEffectBadgeFactory effectBadgeFactory) {
        this.styleProvider = styleProvider;
        this.effectBadgeFactory = effectBadgeFactory;
    }

    public Label create(SkillEffect effect, double maxWidth) {
        SkillEffectBadge badge = effectBadgeFactory.create(effect);
        Label label = new Label(badge.text());
        label.setStyle(styleProvider.effectBadgeStyle(badge.color()));
        label.setEffect(createGlow(badge.color()));
        label.setMaxWidth(maxWidth);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }

    private DropShadow createGlow(String color) {
        DropShadow glow = new DropShadow();
        glow.setBlurType(BlurType.THREE_PASS_BOX);
        glow.setRadius(8);
        glow.setSpread(0.15);
        glow.setColor(Color.web(color, 0.5));
        return glow;
    }
}












