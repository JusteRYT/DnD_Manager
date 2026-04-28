package com.example.dnd_manager.info.skills.popup;

import com.example.dnd_manager.info.skills.view.SkillCardStyleProvider;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.util.Duration;

public class SkillPopupInteractionController {

    private static final double POPUP_WIDTH = 380;
    private static final double POPUP_GAP = 8;
    private static final double POPUP_MINIMUM_Y = 20;

    private final Popup popup;
    private final VBox card;
    private final Label triggerLabel;
    private final SkillCardStyleProvider styleProvider;
    private final SkillPopupPositionCalculator positionCalculator;
    private final Timeline appearanceTimer = new Timeline();

    private boolean mouseInPopup;
    private boolean mouseInTrigger;

    public SkillPopupInteractionController(
            Popup popup,
            VBox card,
            Label triggerLabel,
            SkillCardStyleProvider styleProvider,
            SkillPopupPositionCalculator positionCalculator
    ) {
        this.popup = popup;
        this.card = card;
        this.triggerLabel = triggerLabel;
        this.styleProvider = styleProvider;
        this.positionCalculator = positionCalculator;
    }

    public void installCardHover() {
        card.setOnMouseEntered(e -> card.setStyle(styleProvider.cardHoverStyle()));
        card.setOnMouseExited(e -> card.setStyle(styleProvider.cardIdleStyle()));
    }

    public void installTriggerHover() {
        appearanceTimer.getKeyFrames().add(new KeyFrame(Duration.millis(350), ae -> showPopup()));

        triggerLabel.setOnMouseEntered(e -> {
            mouseInTrigger = true;
            appearanceTimer.playFromStart();
        });

        triggerLabel.setOnMouseExited(e -> {
            mouseInTrigger = false;
            appearanceTimer.stop();
            new Timeline(new KeyFrame(Duration.millis(150), ae -> closeIfOutside())).play();
        });
    }

    public void markPopupEntered() {
        mouseInPopup = true;
    }

    public void markPopupExited() {
        mouseInPopup = false;
        closeIfOutside();
    }

    private void showPopup() {
        if (!mouseInTrigger || popup.isShowing()) {
            return;
        }

        SkillPopupPosition position = positionCalculator.calculate(
                triggerLabel.localToScreen(triggerLabel.getBoundsInLocal()).getMaxX(),
                triggerLabel.localToScreen(triggerLabel.getBoundsInLocal()).getMinY(),
                card.localToScreen(card.getBoundsInLocal()).getMinX(),
                Screen.getPrimary().getVisualBounds().getMaxX(),
                POPUP_WIDTH,
                POPUP_GAP,
                POPUP_MINIMUM_Y
        );

        popup.show(card.getScene().getWindow(), position.x(), position.y());
    }

    private void closeIfOutside() {
        if (!mouseInTrigger && !mouseInPopup) {
            popup.hide();
        }
    }
}












