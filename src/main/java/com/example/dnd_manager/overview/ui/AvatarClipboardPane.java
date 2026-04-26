package com.example.dnd_manager.overview.ui;

import com.example.dnd_manager.lang.I18n;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Avatar pane with hover styling and click-to-copy image behavior.
 */
public final class AvatarClipboardPane {

    private AvatarClipboardPane() {
    }

    public static StackPane create(Image image) {
        StackPane avatarContainer = new StackPane();
        avatarContainer.setPadding(new Insets(3));
        avatarContainer.setCursor(Cursor.HAND);

        javafx.scene.shape.Rectangle view = new javafx.scene.shape.Rectangle();
        if (image != null) {
            view.setFill(new javafx.scene.paint.ImagePattern(image));
        }

        view.widthProperty().bind(avatarContainer.widthProperty().subtract(10));
        view.heightProperty().bind(avatarContainer.heightProperty().subtract(10));
        view.setArcWidth(12);
        view.setArcHeight(12);
        avatarContainer.getChildren().add(view);

        String baseStyle = """
        -fx-background-color: #2b2b2b;
        -fx-background-radius: 8;
        -fx-border-color: #c89b3c;
        -fx-border-radius: 8;
        -fx-border-width: 2;
        -fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.3), 15, 0, 0, 0);
        """;

        String hoverStyle = """
        -fx-background-color: #2b2b2b;
        -fx-background-radius: 8;
        -fx-border-color: #f5b741;
        -fx-border-radius: 8;
        -fx-border-width: 2;
        -fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.8), 25, 0, 0, 0);
        """;

        avatarContainer.setStyle(baseStyle);
        avatarContainer.setOnMouseEntered(e -> avatarContainer.setStyle(hoverStyle));
        avatarContainer.setOnMouseExited(e -> avatarContainer.setStyle(baseStyle));
        avatarContainer.setOnMouseClicked(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putImage(image);
            clipboard.setContent(content);
            showCopiedNotification(avatarContainer);
        });

        return avatarContainer;
    }

    private static void showCopiedNotification(StackPane container) {
        Label notification = createNotificationLabel();
        notification.maxWidthProperty().bind(container.widthProperty().subtract(20));
        container.getChildren().add(notification);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), notification);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), notification);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(actionEvent -> container.getChildren().remove(notification));

        fadeIn.setOnFinished(e -> pause.play());
        pause.setOnFinished(e -> fadeOut.play());
        fadeIn.play();
    }

    private static Label createNotificationLabel() {
        Label notification = new Label(I18n.t("text.clipboardImage"));
        notification.setWrapText(true);
        notification.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        notification.setAlignment(Pos.CENTER);
        notification.setStyle("""
                -fx-background-color: rgba(0, 0, 0, 0.8);
                -fx-text-fill: #c89b3c;
                -fx-font-weight: bold;
                -fx-padding: 10 15;
                -fx-background-radius: 6;
                -fx-border-color: #c89b3c;
                -fx-border-radius: 6;
                -fx-font-size: 13px;
            """);
        notification.setMouseTransparent(true);
        return notification;
    }
}

