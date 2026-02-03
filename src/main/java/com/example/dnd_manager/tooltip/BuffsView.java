package com.example.dnd_manager.tooltip;

import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.buff_debuff.BuffPopupView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Popup;

import java.net.URL;
import java.util.List;

/**
 * Displays character buffs and debuffs as icons with tooltips.
 */
public class BuffsView extends FlowPane {

    private static final String DEFAULT_ICON_PATH =
            "/com/example/dnd_manager/icon/images.png";

    public BuffsView(List<Buff> buffs) {
        super(8, 8);
        setPrefWrapLength(400);

        for (Buff buff : buffs) {
            ImageView icon = createIcon(buff);

            Popup popup = new Popup();
            popup.getContent().add(new BuffPopupView(buff));
            popup.setAutoFix(true);
            popup.setAutoHide(false);

            icon.setOnMouseEntered(e -> {
                var bounds = icon.localToScreen(icon.getBoundsInLocal());
                popup.show(
                        icon.getScene().getWindow(),
                        bounds.getMaxX() + 10,
                        bounds.getMinY()
                );
            });

            icon.setOnMouseExited(e -> popup.hide());

            getChildren().add(icon);
        }
    }

    private ImageView createIcon(Buff buff) {
        Image image;

        String path = buff.iconPath();
        if (path == null || path.isBlank()) {
            URL resource = getClass().getResource(DEFAULT_ICON_PATH);

            if (resource == null) {
                return new ImageView();
            }

            image = new Image(resource.toExternalForm());
        } else {
            image = new Image("file:" + path);
        }

        ImageView icon = new ImageView(image);
        icon.setFitWidth(40);
        icon.setFitHeight(40);
        icon.setPreserveRatio(true);

        return icon;
    }
}