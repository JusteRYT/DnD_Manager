package com.example.dnd_manager.info.buff_debuff;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Popup;

import java.util.Objects;

/**
 * Factory for buff/debuff icon views.
 */
public final class BuffIconViewFactory {

    private static final String DEFAULT_ICON_PATH = "/com/example/dnd_manager/icon/images.png";

    private BuffIconViewFactory() {
    }

    public static ImageView create(Buff buff, BuffColumnStyle style) {
        Image image;

        try {
            if (buff.iconPath() == null || buff.iconPath().isBlank()) {
                image = new Image(
                        BuffIconViewFactory.class.getResource(DEFAULT_ICON_PATH).toExternalForm()
                );
            } else {
                image = new Image("file:" + buff.iconPath());
            }
        } catch (Exception e) {
            image = new Image(Objects.requireNonNull(BuffIconViewFactory.class.getResource(DEFAULT_ICON_PATH)).toExternalForm());
        }

        ImageView icon = new ImageView(image);
        icon.setFitWidth(40);
        icon.setFitHeight(40);
        icon.setPreserveRatio(true);

        Popup popup = new Popup();
        popup.getContent().add(new BuffPopupView(buff));
        popup.setAutoFix(true);

        icon.setOnMouseEntered(e -> {
            var bounds = icon.localToScreen(icon.getBoundsInLocal());
            popup.show(
                    icon.getScene().getWindow(),
                    bounds.getMaxX() + 10,
                    bounds.getMinY()
            );
        });

        icon.setOnMouseExited(e -> popup.hide());

        icon.setStyle("""
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, %s, 6, 0.6, 0, 0);
        """.formatted(style.accentColor()));

        return icon;
    }
}
