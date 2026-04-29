package com.example.dnd_manager.theme.scroll;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

public final class AppScrollPaneFactory {

    private AppScrollPaneFactory() {}

    public static ScrollPane defaultPane(Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        Color colorBgPrimary = Color.TRANSPARENT;
        Color colorBgSecondary = Color.web("#11172a");
        Color colorAccent = Color.web("#b7c9dd");

        LinearGradient thumbGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#dfe6ec")),
                new Stop(1, Color.web("#829cbc"))
        );

        DropShadow glow = new DropShadow(5, Color.web("rgba(175, 196, 216, 0.14)"));

        scrollPane.setBackground(new Background(new BackgroundFill(colorBgPrimary, CornerRadii.EMPTY, null)));
        scrollPane.setStyle("""
                -fx-background: transparent;
                -fx-background-color: transparent;
                -fx-background-insets: 0;
                -fx-padding: 0;
                -fx-control-inner-background: transparent;
                """);

        scrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    scrollPane.applyCss();

                    Node viewport = scrollPane.lookup(".viewport");
                    if (viewport instanceof Region vp) {
                        vp.setBackground(new Background(new BackgroundFill(colorBgPrimary, CornerRadii.EMPTY, null)));
                        vp.setBorder(null);
                    }

                    scrollPane.lookupAll(".scroll-bar").forEach(node -> {
                        if (node instanceof ScrollBar bar) {
                            bar.setBackground(new Background(new BackgroundFill(colorBgSecondary, new CornerRadii(6), null)));

                            // Track
                            Region track = (Region) bar.lookup(".track");
                            if (track != null) {
                                track.setBackground(new Background(new BackgroundFill(colorBgSecondary, new CornerRadii(6), null)));
                            }

                            // Thumb
                            Region thumb = (Region) bar.lookup(".thumb");
                            if (thumb != null) {
                                thumb.setBackground(new Background(new BackgroundFill(thumbGradient, new CornerRadii(6), null)));
                                thumb.setEffect(glow); // Добавляем свечение

                                thumb.setOnMouseEntered(e -> thumb.setEffect(new DropShadow(7, Color.web("rgba(175, 196, 216, 0.24)"))));
                                thumb.setOnMouseExited(e -> thumb.setEffect(glow));
                            }

                            // Стрелки
                            createCustomArrow(bar, true, colorAccent, colorBgSecondary, glow);
                            createCustomArrow(bar, false, colorAccent, colorBgSecondary, glow);
                        }
                    });

                    Region corner = (Region) scrollPane.lookup(".corner");
                    if (corner != null) {
                        corner.setBackground(new Background(new BackgroundFill(colorBgPrimary, CornerRadii.EMPTY, null)));
                    }
                });
            }
        });

        return scrollPane;
    }

    private static void createCustomArrow(ScrollBar bar, boolean decrement, Color accent, Color bg, DropShadow glow) {
        Region arrow = (Region) bar.lookup(decrement ? ".decrement-button" : ".increment-button");
        if (arrow != null) {
            Background baseBg = new Background(new BackgroundFill(bg, new CornerRadii(3), null));
            Border baseBorder = new Border(new BorderStroke(accent, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1)));

            arrow.setBackground(baseBg);
            arrow.setBorder(baseBorder);

            arrow.setOnMouseEntered(e -> {
                arrow.setEffect(glow);
                arrow.setBackground(new Background(new BackgroundFill(accent.deriveColor(0, 1, 1, 0.1), new CornerRadii(3), null)));
            });
            arrow.setOnMouseExited(e -> {
                arrow.setEffect(null);
                arrow.setBackground(baseBg);
            });
        }
    }
}











