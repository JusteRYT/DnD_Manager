package com.example.dnd_manager.theme.button;

import com.example.dnd_manager.theme.utils.Utils;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

class ValueAdjustGradientButtonBuilder {

    private final GradientButtonStyleProvider styleProvider;

    ValueAdjustGradientButtonBuilder(GradientButtonStyleProvider styleProvider) {
        this.styleProvider = styleProvider;
    }

    Button build(boolean isPlus, int size, String baseColor, String hoverColor) {
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);

        StackPane icon = Utils.createAdjustIcon(isPlus, size);
        button.setGraphic(icon);

        String glowColor = isPlus ? "rgba(255, 140, 0, 0.5)" : "rgba(255, 0, 0, 0.4)";
        String baseStyle = styleProvider.valueAdjustBaseStyle(baseColor, glowColor);
        String hoverStyle = styleProvider.valueAdjustHoverStyle(hoverColor, glowColor);

        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        button.setOnMousePressed(e -> {
            button.setTranslateY(1);
            button.setStyle(baseStyle + "-fx-effect: null; -fx-background-color: derive(" + baseColor + ", -15%);");
        });
        button.setOnMouseReleased(e -> {
            button.setTranslateY(0);
            button.setStyle(button.isHover() ? hoverStyle : baseStyle);
        });

        return button;
    }
}












