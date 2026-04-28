package com.example.dnd_manager.theme.button;

import com.example.dnd_manager.theme.AppTheme;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Duration;

class AddEffectGradientButtonBuilder {

    Button build() {
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
}












