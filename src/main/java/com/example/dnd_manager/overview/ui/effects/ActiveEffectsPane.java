package com.example.dnd_manager.overview.ui.effects;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;

/**
 * Reusable pane for displaying active item effects of a character.
 */
public class ActiveEffectsPane extends VBox {

    private final ActiveEffectsService activeEffectsService;
    private final ActiveEffectsStyleProvider styleProvider;
    private final ActiveEffectIconResolver iconResolver;

    public ActiveEffectsPane(Character character) {
        this(character, new ActiveEffectsService(), new ActiveEffectsStyleProvider(), new CharacterAssetActiveEffectIconResolver());
    }

    ActiveEffectsPane(
            Character character,
            ActiveEffectsService activeEffectsService,
            ActiveEffectsStyleProvider styleProvider,
            ActiveEffectIconResolver iconResolver
    ) {
        this.activeEffectsService = Objects.requireNonNull(activeEffectsService, "activeEffectsService must not be null");
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
        this.iconResolver = Objects.requireNonNull(iconResolver, "iconResolver must not be null");
        setSpacing(4);
        setPadding(new Insets(4, 0, 0, 0));
        rebuild(character);
    }

    public void rebuild(Character character) {
        getChildren().clear();
        List<ActiveEffectBadge> badges = activeEffectsService.collect(character);

        FlowPane buffsPane = new FlowPane(6, 6);
        for (ActiveEffectBadge badge : badges) {
            buffsPane.getChildren().add(createEffectLabel(badge, character));
        }

        if (buffsPane.getChildren().isEmpty()) {
            return;
        }

        Label title = new Label(I18n.t("label.activeEffects"));
        title.setStyle(styleProvider.titleStyle());
        getChildren().addAll(title, buffsPane);
    }

    private Label createEffectLabel(ActiveEffectBadge badge, Character character) {
        Label label = new Label(badge.text());
        label.setStyle(styleProvider.effectLabelStyle());

        ImageView icon = iconResolver.resolve(character, badge.iconPath());
        if (icon != null) {
            label.setGraphic(icon);
        }
        return label;
    }
}












