package com.example.dnd_manager.theme.button;

import javafx.scene.control.Button;

class PrimaryGradientButtonBuilder {

    private static final int DEFAULT_FONT_SIZE = 14;

    private final GradientButtonStyleProvider styleProvider;

    PrimaryGradientButtonBuilder(GradientButtonStyleProvider styleProvider) {
        this.styleProvider = styleProvider;
    }

    Button buildIconButton(String text) {
        Button button = new Button(text);
        button.setStyle(styleProvider.primaryGradientStyle(DEFAULT_FONT_SIZE, false));
        button.setOnMouseEntered(e -> button.setStyle(styleProvider.primaryGradientStyle(DEFAULT_FONT_SIZE, true)));
        button.setOnMouseExited(e -> button.setStyle(styleProvider.primaryGradientStyle(DEFAULT_FONT_SIZE, false)));
        button.setOnMousePressed(e -> button.setTranslateY(1));
        button.setOnMouseReleased(e -> button.setTranslateY(0));
        return button;
    }

    Button buildPrimaryButton(String text, int width, int height, int fontSize) {
        Button button = new Button(text);
        button.setPrefSize(width, height);
        button.setStyle(styleProvider.primaryGradientStyle(fontSize, false));
        button.setOnMouseEntered(e -> button.setStyle(styleProvider.primaryGradientStyle(fontSize, true)));
        button.setOnMouseExited(e -> button.setStyle(styleProvider.primaryGradientStyle(fontSize, false)));
        button.setOnMousePressed(e -> {
            button.setTranslateY(2);
            button.setStyle(styleProvider.primaryGradientStyle(fontSize, false) + "-fx-background-color: #e67e22;");
        });
        button.setOnMouseReleased(e -> {
            button.setTranslateY(0);
            button.setStyle(button.isHover()
                    ? styleProvider.primaryGradientStyle(fontSize, true)
                    : styleProvider.primaryGradientStyle(fontSize, false));
        });
        return button;
    }
}












