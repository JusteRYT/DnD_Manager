package com.example.dnd_manager.info.buff_debuff;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Popup;

import java.util.Objects;

/**
 * Factory for buff/debuff icon views.
 */
public final class BuffIconViewFactory {

    private static final String DEFAULT_ICON_PATH =
            "/com/example/dnd_manager/icon/images.png";

    private static final int DEFAULT_ICON_SIZE = 40;

    private BuffIconViewFactory() {
    }

    /**
     * Creates a buff/debuff icon with default size.
     */
    public static ImageView create(Buff buff, BuffColumnStyle style) {
        return createInternal(buff, style, DEFAULT_ICON_SIZE);
    }

    /**
     * Creates a buff/debuff icon with custom size.
     */
    public static ImageView create(Buff buff, BuffColumnStyle style, int size) {
        return createInternal(buff, style, size);
    }

    /**
     * Shared icon creation logic.
     */
    private static ImageView createInternal(
            Buff buff,
            BuffColumnStyle style,
            int size
    ) {
        Image image;

        try {
            if (buff.iconPath() == null || buff.iconPath().isBlank()) {
                image = new Image(
                        Objects.requireNonNull(
                                BuffIconViewFactory.class.getResource(DEFAULT_ICON_PATH)
                        ).toExternalForm()
                );
            } else {
                image = new Image("file:" + buff.iconPath());
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
