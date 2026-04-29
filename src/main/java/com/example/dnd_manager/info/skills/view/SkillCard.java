package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.editors.common.EntityEditorButtonFactory;
import com.example.dnd_manager.info.skills.model.Skill;
import com.example.dnd_manager.info.skills.model.SkillEffect;
import com.example.dnd_manager.infrastructure.assets.CharacterAssetResolver;
import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import lombok.Getter;


/**
 * Visual card representation of a skill.
 * Layout is centered with fixed-size icon, name, meta info, description and remove button.
 */
@Getter
public class SkillCard extends VBox {
    private final Skill skill;
    private final SkillCardStyleProvider styleProvider = new SkillCardStyleProvider();
    private final SkillEffectBadgeFactory effectBadgeFactory = new SkillEffectBadgeFactory();

    public SkillCard(Skill skill, Runnable onRemove, Runnable onEdit, Character character) {
        this.skill = skill;
        setSpacing(9);
        setAlignment(Pos.TOP_LEFT);
        setPrefWidth(320);
        setMinWidth(320);
        setMinHeight(248);
        setStyle(styleProvider.cardIdleStyle());
        setOnMouseEntered(e -> setStyle(styleProvider.cardHoverStyle()));
        setOnMouseExited(e -> setStyle(styleProvider.cardIdleStyle()));

        ImageView iconView = new ImageView();
        iconView.setFitWidth(64);
        iconView.setFitHeight(64);
        iconView.setPreserveRatio(true);
        iconView.setImage(chooseIcon(skill, character));

        iconView.setStyle("-fx-effect: dropshadow(two-pass-box, black, 10, 0, 0, 0);");

        StackPane iconFrame = new StackPane(iconView);
        iconFrame.setMinSize(78, 78);
        iconFrame.setPrefSize(78, 78);
        iconFrame.setMaxSize(78, 78);
        iconFrame.setStyle(styleProvider.iconFrameStyle());

        Label nameCaption = createCaption(I18n.t("skill.attrName").toUpperCase());
        Label name = new Label(skill.name().toUpperCase());
        name.setStyle(styleProvider.nameStyle());
        name.setWrapText(true);
        name.setAlignment(Pos.CENTER_LEFT);
        name.setTextAlignment(TextAlignment.LEFT);
        name.setMaxWidth(190);

        Label activationCaption = createCaption(I18n.t("skill.attrActivation").toUpperCase());
        Label activation = new Label(skill.activationDisplayName());
        activation.setStyle(styleProvider.activationBadgeStyle());

        HBox activationRow = new HBox(7, activationCaption, activation);
        activationRow.setAlignment(Pos.CENTER_LEFT);

        VBox titleColumn = new VBox(4, nameCaption, name, activationRow);
        titleColumn.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(titleColumn, Priority.ALWAYS);

        HBox header = new HBox(10, iconFrame, titleColumn);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setMaxWidth(Double.MAX_VALUE);
        header.setStyle(styleProvider.headerBandStyle());

        VBox effectsPanel = buildEffectsPanel();
        VBox descriptionPanel = buildDescriptionPanel(skill.description());

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setMaxWidth(Double.MAX_VALUE);
        Button editBtn = EntityEditorButtonFactory.secondary(I18n.t("button.editEditor"), 116);
        editBtn.setOnAction(e -> onEdit.run());

        Button removeBtn = EntityEditorButtonFactory.danger("x");
        removeBtn.setOnAction(e -> onRemove.run());
        removeBtn.setFocusTraversable(false);

        actions.getChildren().addAll(editBtn, removeBtn);

        getChildren().addAll(header, effectsPanel, descriptionPanel, new Region() {{
            VBox.setVgrow(this, Priority.ALWAYS);
        }}, actions);
    }

    private VBox buildEffectsPanel() {
        Label caption = createCaption(I18n.t("skill.attrEffects").toUpperCase() + " (" + skill.effects().size() + ")");

        FlowPane effectsPane = new FlowPane(6, 6);
        effectsPane.setAlignment(Pos.CENTER_LEFT);
        effectsPane.setPrefWrapLength(286);
        for (SkillEffect effect : skill.effects()) {
            SkillEffectBadge badge = effectBadgeFactory.create(effect);
            Label tag = new Label(badge.text());
            tag.setStyle(styleProvider.effectBadgeStyle(badge.color()));
            tag.setWrapText(true);
            tag.setTextAlignment(TextAlignment.CENTER);
            effectsPane.getChildren().add(tag);
        }
        if (skill.effects().isEmpty()) {
            Label empty = new Label(I18n.t("dialogDescription.emptyValue"));
            empty.setStyle(styleProvider.briefDescriptionStyle());
            effectsPane.getChildren().add(empty);
        }

        VBox panel = new VBox(5, caption, effectsPane);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setMaxWidth(Double.MAX_VALUE);
        panel.setStyle(styleProvider.effectsPanelStyle());
        return panel;
    }

    private VBox buildDescriptionPanel(String description) {
        Label caption = createCaption(I18n.t("textFieldLabel.description").toUpperCase());

        Label desc = new Label(description == null || description.isBlank() ? I18n.t("dialogDescription.emptyValue") : description);
        desc.setStyle(styleProvider.briefDescriptionStyle());
        desc.setWrapText(true);
        desc.setTextAlignment(TextAlignment.LEFT);
        desc.setMaxHeight(54);

        VBox panel = new VBox(5, caption, desc);
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setMaxWidth(Double.MAX_VALUE);
        panel.setStyle(styleProvider.descriptionPanelStyle());
        panel.setMinHeight(78);
        return panel;
    }

    private Label createCaption(String text) {
        Label caption = new Label(text);
        caption.setStyle(styleProvider.sectionCaptionStyle());
        return caption;
    }

    private Image chooseIcon(Skill skill, Character character) {
        return getImage(character, skill.iconPath());
    }

    public static Image getImage(Character character, String iconPath) {
       return CharacterAssetResolver.getImage(character, iconPath, 60, 60);
    }
}











