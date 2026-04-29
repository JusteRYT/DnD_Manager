package com.example.dnd_manager.theme.button;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.screen.assets.AssetManagerScreen;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.window.WindowFactory;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Factory for icon-first buttons and pickers.
 */
final class IconButtonFactory {
    private static final Logger log = LoggerFactory.getLogger(IconButtonFactory.class);

    private IconButtonFactory() {
    }

    static Button hudIconButton(int size, String iconPath) {
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);
        button.setPickOnBounds(true);

        String borderAccent = AppTheme.BUTTON_PRIMARY;
        String glowColor = "rgba(175, 196, 216, 0.28)";

        String colorHoverBg = "#11172a";
        String colorPressedBg = "#070b14";
        String shadowCss = "-fx-effect: dropshadow(three-pass-box, %s, 12, 0.1, 0, 0);".formatted(glowColor);

        String hoverStyle = """
        -fx-background-color: %s;
        -fx-background-radius: 6;
        -fx-border-color: %s;
        -fx-border-radius: 6;
        -fx-border-width: 1.2;
        -fx-cursor: hand;
        %s
        """.formatted(colorHoverBg, borderAccent, shadowCss);

        String pressedStyle = """
        -fx-background-color: %s;
        -fx-background-radius: 6;
        -fx-border-color: %s;
        -fx-border-radius: 6;
        -fx-border-width: 1.2;
        -fx-effect: innershadow(three-pass-box, rgba(0,0,0,0.8), 8, 0, 0, 1);
        """.formatted(colorPressedBg, borderAccent);

        button.setStyle(hoverStyle);

        ColorAdjust iconEffect = new ColorAdjust();
        iconEffect.setBrightness(0.9);
        iconEffect.setSaturation(0.4);

        button.setOnMousePressed(e -> {
            button.setStyle(pressedStyle);
            button.setTranslateY(1.0);
        });
        button.setOnMouseReleased(e -> {
            button.setTranslateY(0);
            button.setStyle(hoverStyle);
        });

        try {
            Image img = new Image(Objects.requireNonNull(IconButtonFactory.class.getResource(iconPath)).toExternalForm());
            ImageView icon = new ImageView(img);
            icon.setFitWidth(size * 0.45);
            icon.setFitHeight(size * 0.45);
            icon.setPreserveRatio(true);
            icon.setMouseTransparent(true);
            icon.setEffect(iconEffect);
            button.setGraphic(icon);
        } catch (Exception e) {
            log.error("Could not load icon: {}", iconPath, e);
        }

        return button;
    }

    static Button assetPickerButton() {
        Button btn = new Button(I18n.t("button.Assets"));
        btn.setPrefWidth(110);

        String accent = AppTheme.TEXT_ACCENT;
        String baseStyle = """
        -fx-background-color: transparent;
        -fx-text-fill: %s;
        -fx-font-family: 'Cinzel';
        -fx-font-size: 13px;
        -fx-font-weight: bold;
        -fx-border-color: #293550;
        -fx-border-radius: 6;
        -fx-background-radius: 6;
        -fx-padding: 6 14;
        -fx-cursor: hand;
        """.formatted(accent);

        String hoverStyle = """
        -fx-background-color: rgba(175, 196, 216, 0.08);
        -fx-text-fill: %s;
        -fx-font-family: 'Cinzel';
        -fx-font-size: 13px;
        -fx-font-weight: bold;
        -fx-border-color: %s;
        -fx-border-radius: 6;
        -fx-background-radius: 6;
        -fx-padding: 6 14;
        -fx-cursor: hand;
        """.formatted(accent, accent);

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        btn.setOnMousePressed(e -> btn.setTranslateY(1));
        btn.setOnMouseReleased(e -> btn.setTranslateY(0));

        return btn;
    }

    static void attachAssetPicker(Button button, Consumer<String> onPathSelected) {
        button.setOnAction(e -> {
            Stage owner = (Stage) button.getScene().getWindow();
            AssetManagerScreen picker = new AssetManagerScreen(owner, selectedPath ->
                    onPathSelected.accept(selectedPath.toString()));
            WindowFactory.openModal(owner, picker, 1100, 750);
        });
    }
}












