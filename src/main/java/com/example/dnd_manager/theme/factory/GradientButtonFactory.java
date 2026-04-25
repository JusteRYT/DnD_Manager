package com.example.dnd_manager.theme.factory;

import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.utils.Utils;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Factory for gradient and emphasis-style buttons.
 */
final class GradientButtonFactory {
    private static final Logger log = LoggerFactory.getLogger(GradientButtonFactory.class);

    private GradientButtonFactory() {
    }

    static Button createValueAdjustButton(boolean isPlus, int size, String baseColor, String hoverColor) {
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);

        StackPane icon = Utils.createAdjustIcon(isPlus, size);
        button.setGraphic(icon);

        String glowColor = isPlus ? "rgba(255, 140, 0, 0.5)" : "rgba(255, 0, 0, 0.4)";

        String commonStyle = """
            -fx-background-radius: 4;
            -fx-border-radius: 4;
            -fx-border-width: 1;
            -fx-cursor: hand;
            -fx-padding: 0;
            """;

        String baseStyle = commonStyle + """
            -fx-background-color: linear-gradient(to bottom, %1$s, derive(%1$s, -20%%));
            -fx-border-color: derive(%1$s, -30%%);
            -fx-effect: dropshadow(three-pass-box, %2$s, 6, 0, 0, 0);
            """.formatted(baseColor, glowColor);

        String hoverStyle = commonStyle + """
            -fx-background-color: linear-gradient(to bottom, derive(%1$s, 20%%), %1$s);
            -fx-border-color: %1$s;
            -fx-effect: dropshadow(three-pass-box, %2$s, 12, 0, 0, 0);
            """.formatted(hoverColor, glowColor);

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

    static Button addEffectButton() {
        Button button = new Button("+");
        button.setMinSize(32, 32);
        button.setMaxSize(32, 32);

        Color colorSecondary = Color.web(AppTheme.BACKGROUND_SECONDARY);
        Color baseGold = Color.web(AppTheme.BUTTON_PRIMARY);
        Color colorAccent = baseGold.deriveColor(0, 1.2, 1.25, 1);
        Color colorTextNormal = Color.web(AppTheme.BUTTON_PRIMARY);
        Color colorTextHover = Color.web(AppTheme.BACKGROUND_PRIMARY);

        button.setStyle("""
                    -fx-font-weight: bold;
                    -fx-font-size: 18px;
                    -fx-background-radius: 50;
                    -fx-background-color: %s;
                    -fx-border-color: %s;
                    -fx-border-radius: 50;
                    -fx-border-width: 1.5;
                    -fx-cursor: hand;
                    -fx-padding: 0 0 2 0;
                """.formatted(AppTheme.BACKGROUND_SECONDARY, AppTheme.BUTTON_PRIMARY));

        button.setBackground(new Background(new BackgroundFill(colorSecondary, new CornerRadii(50), Insets.EMPTY)));
        button.setTextFill(colorTextNormal);

        Transition transition = new Transition() {
            {
                setCycleDuration(Duration.millis(300));
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double fraction) {
                Color mixedBg = colorSecondary.interpolate(colorAccent, fraction);
                button.setBackground(new Background(new BackgroundFill(mixedBg, new CornerRadii(50), Insets.EMPTY)));

                Color mixedText = colorTextNormal.interpolate(colorTextHover, fraction);
                button.setTextFill(mixedText);
            }
        };

        button.setOnMouseEntered(e -> {
            transition.setRate(1.0);
            transition.play();
        });
        button.setOnMouseExited(e -> {
            transition.setRate(-1.0);
            transition.play();
        });
        button.setOnMousePressed(e -> button.setScaleX(0.92));
        button.setOnMouseReleased(e -> button.setScaleX(1.0));

        return button;
    }

    static Button actionEditIcon(String iconPath, int size) {
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);

        String baseStyle = """
                    -fx-background-color: linear-gradient(to bottom, #FFC107, #FF8C00);
                    -fx-background-radius: 4;
                    -fx-cursor: hand;
                    -fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.3), 8, 0, 0, 0);
                """;

        String hoverStyle = """
                    -fx-background-color: linear-gradient(to bottom, #ffd54f, #ffa726);
                    -fx-background-radius: 4;
                    -fx-cursor: hand;
                    -fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.6), 12, 0, 0, 0);
                """;

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

        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                ImageView icon = new ImageView(new Image(Objects.requireNonNull(
                        GradientButtonFactory.class.getResource(iconPath)).toExternalForm()));
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

        return button;
    }

    static Button deleteButton(int size) {
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);

        StackPane icon = Utils.createAdjustIcon(false, size);
        button.setGraphic(icon);

        final boolean[] isActive = {false};
        String colorNormal = AppTheme.BUTTON_REMOVE;
        String colorActive = AppTheme.BUTTON_DANGER;
        String colorHover = AppTheme.BUTTON_REMOVE_HOVER;

        String commonStyle = """
            -fx-background-radius: 4;
            -fx-border-radius: 4;
            -fx-border-width: 1;
            -fx-cursor: hand;
            -fx-padding: 0;
            """;

        BiFunction<String, Boolean, String> styleBuilder = (color, glowing) -> {
            String glowOpacity = glowing ? "0.6" : "0.3";
            int glowRadius = glowing ? 12 : 6;
            return commonStyle + """
                -fx-background-color: linear-gradient(to bottom, %1$s, derive(%1$s, -20%%));
                -fx-border-color: derive(%1$s, -30%%);
                -fx-effect: dropshadow(three-pass-box, rgba(255, 0, 0, %2$s), %3$d, 0, 0, 0);
                """.formatted(color, glowOpacity, glowRadius);
        };

        button.setStyle(styleBuilder.apply(colorNormal, false));
        button.setOnMouseEntered(e -> {
            if (!isActive[0]) {
                button.setStyle(styleBuilder.apply(colorHover, true));
            }
        });
        button.setOnMouseExited(e ->
                button.setStyle(styleBuilder.apply(isActive[0] ? colorActive : colorNormal, isActive[0])));
        button.setOnAction(e -> {
            isActive[0] = !isActive[0];
            button.setStyle(styleBuilder.apply(isActive[0] ? colorActive : colorHover, isActive[0]));
            button.setUserData(isActive[0]);
            button.setTranslateY(isActive[0] ? 1 : 0);
        });

        return button;
    }

    static Button actionSave(String text) {
        return addIcon(text);
    }

    static Button addIcon(String text) {
        Button button = new Button(text);
        int fontSize = 14;

        button.setStyle(getPrimaryGradientStyle(fontSize, false));
        button.setOnMouseEntered(e -> button.setStyle(getPrimaryGradientStyle(fontSize, true)));
        button.setOnMouseExited(e -> button.setStyle(getPrimaryGradientStyle(fontSize, false)));
        button.setOnMousePressed(e -> button.setTranslateY(1));
        button.setOnMouseReleased(e -> button.setTranslateY(0));

        return button;
    }

    static Button primaryButton(String text, int width, int height, int fontSize) {
        Button button = new Button(text);
        button.setPrefSize(width, height);

        button.setStyle(getPrimaryGradientStyle(fontSize, false));
        button.setOnMouseEntered(e -> button.setStyle(getPrimaryGradientStyle(fontSize, true)));
        button.setOnMouseExited(e -> button.setStyle(getPrimaryGradientStyle(fontSize, false)));
        button.setOnMousePressed(e -> {
            button.setTranslateY(2);
            button.setStyle(getPrimaryGradientStyle(fontSize, false) + "-fx-background-color: #e67e22;");
        });
        button.setOnMouseReleased(e -> {
            button.setTranslateY(0);
            button.setStyle(button.isHover() ? getPrimaryGradientStyle(fontSize, true) : getPrimaryGradientStyle(fontSize, false));
        });

        return button;
    }

    private static String getPrimaryGradientStyle(int fontSize, boolean isHover) {
        String base = """
                -fx-background-color: linear-gradient(to bottom, #FFC107, #FF8C00);
                -fx-text-fill: #222;
                -fx-font-weight: bold;
                -fx-font-size: %dpx;
                -fx-background-radius: 4;
                -fx-cursor: hand;
                """.formatted(fontSize);

        if (isHover) {
            return base + "-fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.6), 15, 0, 0, 0);";
        }
        return base + "-fx-effect: dropshadow(three-pass-box, rgba(255, 140, 0, 0.3), 8, 0, 0, 0);";
    }
}
