package com.example.dnd_manager.theme;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Fully custom ScrollPane with colored background, custom track, thumb, arrow buttons and no white borders.
 */
public final class AppScrollPaneFactory {

    private AppScrollPaneFactory() {}

    public static ScrollPane defaultPane(Node content) {
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Content background
        if (content instanceof Region region) {
            region.setBackground(new Background(new BackgroundFill(
                    Color.web(AppTheme.BACKGROUND_PRIMARY),
                    new CornerRadii(0), null
            )));
        }

        // ScrollPane background + remove default border
        scrollPane.setBackground(new Background(new BackgroundFill(
                Color.web(AppTheme.BACKGROUND_PRIMARY),
                new CornerRadii(0), null
        )));
        scrollPane.setStyle("""
                -fx-background: %s;
                -fx-background-insets: 0;
                -fx-padding: 0;
                """.formatted(AppTheme.BACKGROUND_PRIMARY));

        // После рендера кастомизируем ScrollBars и viewport
        scrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    scrollPane.applyCss();

                    // Убираем border и фон viewport
                    Node viewport = scrollPane.lookup(".viewport");
                    if (viewport instanceof Region vp) {
                        vp.setBackground(new Background(new BackgroundFill(
                                Color.web(AppTheme.BACKGROUND_PRIMARY),
                                new CornerRadii(0), null
                        )));
                        vp.setBorder(null);
                    }

                    scrollPane.lookupAll(".scroll-bar").forEach(node -> {
                        if (node instanceof ScrollBar bar) {
                            bar.setBackground(new Background(new BackgroundFill(
                                    Color.web(AppTheme.BACKGROUND_SECONDARY), new CornerRadii(6), null
                            )));
                            bar.setOpacity(1);

                            // Track
                            Region track = (Region) bar.lookup(".track");
                            if (track != null) {
                                track.setBackground(new Background(new BackgroundFill(
                                        Color.web(AppTheme.BACKGROUND_SECONDARY), new CornerRadii(6), null
                                )));
                            }

                            // Thumb
                            Region thumb = (Region) bar.lookup(".thumb");
                            if (thumb != null) {
                                thumb.setBackground(new Background(new BackgroundFill(
                                        Color.web(AppTheme.BUTTON_PRIMARY), new CornerRadii(6), null
                                )));
                            }

                            // Стрелки
                            createCustomArrow(bar, true);
                            createCustomArrow(bar, false);
                        }
                    });

                    // Corner
                    Region corner = (Region) scrollPane.lookup(".corner");
                    if (corner != null) {
                        corner.setBackground(new Background(new BackgroundFill(
                                Color.web(AppTheme.BACKGROUND_PRIMARY), new CornerRadii(0), null
                        )));
                    }
                });
            }
        });

        return scrollPane;
    }

    private static void createCustomArrow(ScrollBar bar, boolean decrement) {
        Region arrow = (Region) bar.lookup(decrement ? ".decrement-button" : ".increment-button");
        if (arrow != null) {
            arrow.setBackground(new Background(new BackgroundFill(
                    Color.web(AppTheme.BACKGROUND_SECONDARY),
                    new CornerRadii(3), null
            )));
            arrow.setBorder(new Border(new BorderStroke(
                    Color.web(AppTheme.BUTTON_PRIMARY),
                    BorderStrokeStyle.SOLID, new CornerRadii(3), BorderWidths.DEFAULT
            )));
        }
    }
}