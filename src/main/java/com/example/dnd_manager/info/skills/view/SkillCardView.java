package com.example.dnd_manager.info.skills.view;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.info.skills.model.Skill;
import com.example.dnd_manager.info.skills.model.SkillEffect;
import com.example.dnd_manager.info.skills.popup.SkillPopupContentBuilder;
import com.example.dnd_manager.info.skills.popup.SkillPopupInteractionController;
import com.example.dnd_manager.info.skills.popup.SkillPopupPositionCalculator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;

public class SkillCardView extends VBox {

    private static final int CARD_WIDTH = 180;
    private static final int CARD_HEIGHT = 310;
    private static final int ICON_SIZE = 70;
    private static final int ICON_SIZE_ = 130;

    private final Popup customPopup = new Popup();
    private final Label briefDesc = new Label();
    private final SkillPopupContentBuilder popupContentBuilder;
    private final SkillPopupInteractionController popupInteractionController;

    public SkillCardView(Skill skill, Character character, InventoryItem sourceItem) {
        this(
                skill,
                character,
                sourceItem,
                new SkillCardStyleProvider(),
                new SkillDescriptionTextFactory(),
                new SkillEffectBadgeFactory(),
                new SkillSourceBadgeViewModelFactory(),
                new SkillPopupPositionCalculator()
        );
    }

    SkillCardView(
            Skill skill,
            Character character,
            InventoryItem sourceItem,
            SkillCardStyleProvider styleProvider,
            SkillDescriptionTextFactory descriptionTextFactory,
            SkillEffectBadgeFactory effectBadgeFactory,
            SkillSourceBadgeViewModelFactory sourceBadgeFactory,
            SkillPopupPositionCalculator popupPositionCalculator
    ) {
        SkillEffectLabelFactory effectLabelFactory = new SkillEffectLabelFactory(styleProvider, effectBadgeFactory);
        SkillIconFrameBuilder iconFrameBuilder = new SkillIconFrameBuilder(
                styleProvider,
                new SkillSourceBadgeViewFactory(styleProvider, sourceBadgeFactory)
        );
        this.popupContentBuilder = new SkillPopupContentBuilder(styleProvider, descriptionTextFactory);
        this.popupInteractionController = new SkillPopupInteractionController(
                customPopup,
                this,
                briefDesc,
                styleProvider,
                popupPositionCalculator
        );
        setSpacing(6);
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        setMinSize(CARD_WIDTH, CARD_HEIGHT);
        setMaxWidth(CARD_WIDTH);

        // Устанавливаем начальный вид
        setStyle(styleProvider.cardIdleStyle());

        var iconFrame = iconFrameBuilder.build(skill, character, sourceItem, ICON_SIZE, ICON_SIZE_);

        // --- 2. NAME ---
        Label nameLabel = new Label(skill.name().toUpperCase());
        nameLabel.setStyle(styleProvider.nameStyle());
        nameLabel.setWrapText(true);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMaxWidth(CARD_WIDTH - 20);
        nameLabel.setMinHeight(VBox.USE_PREF_SIZE);

        // --- 3. EFFECTS ---
        FlowPane effectsPane = new FlowPane(5, 5);
        effectsPane.setAlignment(Pos.CENTER);
        effectsPane.setPrefWrapLength(CARD_WIDTH - 20);

        for (SkillEffect effect : skill.effects()) {
            effectsPane.getChildren().add(effectLabelFactory.create(effect, CARD_WIDTH - 25));
        }

        // --- 4. DESCRIPTION ---
        Separator separator = new Separator();
        separator.setOpacity(0.2);
        VBox.setMargin(nameLabel, new Insets(5, 0, 5, 0));

        briefDesc.setText(descriptionTextFactory.briefText(skill));
        briefDesc.setStyle(styleProvider.briefDescriptionStyle());
        briefDesc.setWrapText(true);
        briefDesc.setTextAlignment(TextAlignment.CENTER);
        briefDesc.setCursor(javafx.scene.Cursor.HAND);
        VBox.setVgrow(briefDesc, Priority.ALWAYS);

        getChildren().addAll(iconFrame, nameLabel, effectsPane, separator, briefDesc);

        setupCustomPopup(skill, sourceItem);
        setupInteractions();
    }

    private void setupCustomPopup(Skill skill, InventoryItem sourceItem) {
        VBox popupContent = popupContentBuilder.build(
                skill,
                sourceItem,
                popupInteractionController::markPopupEntered,
                popupInteractionController::markPopupExited
        );

        customPopup.getContent().clear();
        customPopup.getContent().add(popupContent);
    }

    private void setupInteractions() {
        popupInteractionController.installCardHover();
        popupInteractionController.installTriggerHover();
    }
}












