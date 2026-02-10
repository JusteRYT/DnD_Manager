package com.example.dnd_manager.info.buff_debuff;

import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Factory for buff/debuff icon views.
 * Popup now shows reliably on hover.
        */
public final class BuffIconViewFactory {

    private static final String DEFAULT_ICON_PATH =
            "/com/example/dnd_manager/icon/images.png";

    private BuffIconViewFactory() {
    }

    /**
     * Creates a buff/debuff icon with custom size and hover popup.
     */
    public static ImageView create(Buff buff, BuffColumnStyle style, int size, String characterName) {
        Image image;

        try {
            if (buff.iconPath() == null || buff.iconPath().isBlank()) {
                image = new Image(
                        Objects.requireNonNull(
                                BuffIconViewFactory.class.getResource(DEFAULT_ICON_PATH)
                        ).toExternalForm()
                );
            } else {
                image = new Image(CharacterAssetResolver.resolve(characterName, buff.iconPath()));
            }
        } catch (Exception e) {
            image = new Image(
                    Objects.requireNonNull(
                            BuffIconViewFactory.class.getResource(DEFAULT_ICON_PATH)
                    ).toExternalForm()
            );
        }

        ImageView icon = new ImageView(image);
        icon.setFitWidth(size);
        icon.setFitHeight(size);
        icon.setPreserveRatio(true);

        // ===== POPUP =====
        Popup popup = new Popup();
        popup.getContent().add(new BuffPopupView(buff));
        popup.setAutoFix(true);
        popup.setAutoHide(true);

        // Задержка перед показом
        PauseTransition showDelay = new PauseTransition(Duration.millis(250));
        showDelay.setOnFinished(e -> {
            if (!popup.isShowing() && icon.isHover()) {
                var bounds = icon.localToScreen(icon.getBoundsInLocal());
                popup.show(icon.getScene().getWindow(), bounds.getMaxX() + 10, bounds.getMinY());
            }
        });

        icon.hoverProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                showDelay.playFromStart();
            } else {
                showDelay.stop();
                popup.hide();
            }
        });

        icon.setStyle("""
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, %s, 6, 0.6, 0, 0);
        """.formatted(style.accentColor()));

        return icon;
    }
}