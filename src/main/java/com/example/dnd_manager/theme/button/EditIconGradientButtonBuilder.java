package com.example.dnd_manager.theme.button;

import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

class EditIconGradientButtonBuilder {
    private static final Logger log = LoggerFactory.getLogger(EditIconGradientButtonBuilder.class);

    private final GradientButtonStyleProvider styleProvider;

    EditIconGradientButtonBuilder(GradientButtonStyleProvider styleProvider) {
        this.styleProvider = styleProvider;
    }

    Button build(String iconPath, int size) {
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);

        String baseStyle = styleProvider.editIconBaseStyle();
        String hoverStyle = styleProvider.editIconHoverStyle();
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        button.setOnMousePressed(e -> {
            button.setTranslateY(1);
            button.setStyle(baseStyle + "-fx-effect: null;");
        });
        button.setOnMouseReleased(e -> {
            button.setTranslateY(0);
            button.setStyle(button.isHover() ? hoverStyle : baseStyle);
        });

        applyIcon(button, iconPath, size);
        return button;
    }

    private void applyIcon(Button button, String iconPath, int size) {
        if (iconPath == null || iconPath.isEmpty()) {
            return;
        }

        try {
            ImageView icon = new ImageView(new Image(Objects.requireNonNull(
                    EditIconGradientButtonBuilder.class.getResource(iconPath)).toExternalForm()));
            icon.setFitWidth(size * 0.6);
            icon.setFitHeight(size * 0.6);
            icon.setPreserveRatio(true);

            ColorAdjust darken = new ColorAdjust();
            darken.setBrightness(-0.8);
            icon.setEffect(darken);
            button.setGraphic(icon);
        } catch (Exception e) {
            log.error("Error loading edit icon: {}", iconPath, e);
        }
    }
}












