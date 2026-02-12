package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.AppTheme;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

/**
 * UI card component for displaying detailed information about a skill.
 * Fixed-size blocks ensure all cards are aligned in a grid.
 */
public class SkillCardView extends VBox {

    private static final int CARD_WIDTH = 200;
    private static final int CARD_HEIGHT = 260;

    private static final int ICON_SIZE = 80;
    private static final int NAME_HEIGHT = 30;
    private static final int META_HEIGHT = 18;
    private static final int DESCRIPTION_HEIGHT = 70;

    public SkillCardView(Skill skill, String characterName) {
        setSpacing(8);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        setMinSize(CARD_WIDTH, CARD_HEIGHT);
        setMaxSize(CARD_WIDTH, CARD_HEIGHT);

        setStyle("""
                -fx-background-color: %s;
                -fx-background-radius: 14;
                -fx-border-radius: 14;
                -fx-border-color: #3a3a3a;
                """.formatted(AppTheme.BACKGROUND_SECONDARY));

        // ===== ICON =====
        ImageView icon = new ImageView();
        icon.setFitWidth(ICON_SIZE);
        icon.setFitHeight(ICON_SIZE);
        icon.setPreserveRatio(false);
        icon.setSmooth(true);
        if (skill.iconPath() != null && !skill.iconPath().isBlank()) {
            icon.setImage(new Image(CharacterAssetResolver.resolve(characterName, skill.iconPath())));
        }

        StackPane iconContainer = new StackPane(icon);
        iconContainer.setPrefSize(ICON_SIZE, ICON_SIZE);
        iconContainer.setMinSize(ICON_SIZE, ICON_SIZE);
        iconContainer.setMaxSize(ICON_SIZE, ICON_SIZE);
        iconContainer.setAlignment(Pos.CENTER);

        // ===== NAME =====
        Label nameLabel = new Label(skill.name());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #c89b3c;");
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setPrefHeight(NAME_HEIGHT);
        nameLabel.setWrapText(true);

        // ===== ACTIVATION =====
        Label activationLabel = new Label(I18n.t("skillCardView.activation") + skill.activationType());
        activationLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #9cdcfe;");
        activationLabel.setPrefHeight(META_HEIGHT);

        // ===== EFFECTS =====
        TextFlow effectsFlow = new TextFlow();
        effectsFlow.setPrefHeight(META_HEIGHT);

        for (int i = 0; i < skill.effects().size(); i++) {
            SkillEffect effect = skill.effects().get(i);

            Text text = new Text(effect.toString());
            text.setStyle("-fx-font-size: 13px; -fx-fill: " + colorByEffect(effect.getType()) + ";");
            effectsFlow.setTextAlignment(TextAlignment.CENTER);

            effectsFlow.getChildren().add(text);

            if (i < skill.effects().size() - 1) {
                effectsFlow.getChildren().add(new Text("; "));
            }
        }

        // ===== DESCRIPTION =====
        Label descriptionLabel = new Label(skill.description());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setPrefHeight(DESCRIPTION_HEIGHT);
        descriptionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #d4d4d4;");

        Tooltip tooltip = new Tooltip(skill.description());
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(CARD_WIDTH - 20);
        tooltip.setShowDelay(Duration.millis(300));
        tooltip.setHideDelay(Duration.millis(100));
        Tooltip.install(descriptionLabel, tooltip);

        // Добавляем все элементы
        getChildren().addAll(iconContainer, nameLabel, activationLabel, effectsFlow, descriptionLabel);
    }

    private String colorByEffect(String type) {
        if (type.equals(TypeEffects.DAMAGE.name())){
            return AppTheme.EFFECT_DAMAGE;
        } else if (type.equals(TypeEffects.HEAL.name())){
            return AppTheme.EFFECT_HEAL;
        } else {
            return AppTheme.EFFECT_OTHER;
        }
    }
}