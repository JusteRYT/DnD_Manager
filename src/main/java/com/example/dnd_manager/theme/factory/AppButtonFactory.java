package com.example.dnd_manager.theme.factory;

import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.utils.Utils;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Objects;

/**
 * Factory for creating styled application buttons.
 */
public final class AppButtonFactory {

    private static final int DEFAULT_SIZE_FONT = 14;

    private AppButtonFactory() {
    }

    /**
     * Creates primary action button.
     *
     * @param text button text
     * @return styled button
     */
    public static Button primary(String text) {
        Button button = new Button(text);
        acceptColorTheme(button, DEFAULT_SIZE_FONT, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);

        return button;
    }

    /**
     * Creates a compact square button for adjusting values (+/-).
     */
    public static Button createValueAdjustButton(boolean isPlus, int size, String baseColor, String hoverColor) {
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);

        // Рисуем иконку программно (Rectangle вместо текста)
        StackPane icon = Utils.createAdjustIcon(isPlus, size);
        button.setGraphic(icon);

        String commonStyle = """
        -fx-background-radius: 4;
        -fx-border-radius: 4;
        -fx-border-width: 1;
        -fx-cursor: hand;
        -fx-padding: 0;
        """;

        String baseStyle = commonStyle + """
        -fx-background-color: %1$s;
        -fx-border-color: derive(%1$s, -20%%);
        """.formatted(baseColor);

        String hoverStyle = commonStyle + """
        -fx-background-color: %1$s;
        -fx-border-color: derive(%1$s, 30%%);
        -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 0);
        """.formatted(hoverColor);

        button.setStyle(baseStyle);

        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));

        button.setOnMousePressed(e -> {
            button.setTranslateY(1);
            button.setStyle(baseStyle + "-fx-background-color: derive(" + baseColor + ", -15%);");
        });

        button.setOnMouseReleased(e -> {
            button.setTranslateY(0);
            button.setStyle(button.isHover() ? hoverStyle : baseStyle);
        });

        return button;
    }

    /**
     * Creates a flat, clean HUD icon button.
     * Matches the application's flat design language.
     */
    public static Button hudIconButton(int size, String iconPath) {
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);

        // Цвета (используем константу PRIMARY для связи с остальным интерфейсом)
        String primaryColor = AppTheme.BUTTON_PRIMARY; // Твой золотой
        String bgColor = "#2b2b2b";     // Плоский темно-серый (как в TopBar левой части)
        String bgHover = "#383838";     // Чуть светлее для реакции
        String bgPressed = "#222222";   // Темнее для нажатия

        // 1. Базовый стиль: Плоский, ровный, аккуратная рамка
        // border-radius: 6 совпадает с customButton
        String baseStyle = """
            -fx-background-color: %s;
            -fx-background-radius: 6;
            -fx-border-color: %s;
            -fx-border-radius: 6;
            -fx-border-width: 1;
            -fx-cursor: hand;
            """.formatted(bgColor, primaryColor);

        // 2. Ховер: Кнопка чуть светлеет, появляется легкое свечение рамки
        String hoverStyle = """
            -fx-background-color: %s;
            -fx-background-radius: 6;
            -fx-border-color: %s;
            -fx-border-radius: 6;
            -fx-border-width: 1;
            -fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.4), 8, 0, 0, 0);
            """.formatted(bgHover, primaryColor);

        // 3. Нажатие: Убираем свечение, затемняем фон
        String pressedStyle = """
            -fx-background-color: %s;
            -fx-background-radius: 6;
            -fx-border-color: %s;
            -fx-border-radius: 6;
            -fx-border-width: 1;
            -fx-effect: null;
            """.formatted(bgPressed, primaryColor);

        button.setStyle(baseStyle);

        // Логика поведения мыши
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));

        button.setOnMousePressed(e -> {
            button.setStyle(pressedStyle);
            button.setTranslateY(1); // Физический отклик
        });

        button.setOnMouseReleased(e -> {
            button.setStyle(button.isHover() ? hoverStyle : baseStyle);
            button.setTranslateY(0);
        });

        // --- Иконка ---
        if (iconPath != null) {
            try {
                ImageView icon = new ImageView(
                        new Image(Objects.requireNonNull(AppButtonFactory.class.getResource(iconPath)).toExternalForm())
                );

                // Размер иконки ~55% от кнопки, чтобы был "воздух"
                int iconSize = (int) (size * 0.55);
                icon.setFitWidth(iconSize);
                icon.setFitHeight(iconSize);
                icon.setPreserveRatio(true);

                // Эффект: Делаем иконку светло-золотой/белой.
                // Это уберет "грязь" черных PNG на темном фоне.
                ColorAdjust cleanLook = new ColorAdjust();
                cleanLook.setBrightness(0.8); // Высветляем до почти белого
                cleanLook.setContrast(0.2);   // Немного контраста

                icon.setEffect(cleanLook);

                button.setGraphic(icon);
            } catch (Exception e) {
                System.err.println("Could not load icon: " + iconPath);
            }
        }

        return button;
    }

    public static Button customButton(String text, int width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        acceptColorTheme(button, DEFAULT_SIZE_FONT, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);

        return button;
    }

    public static Button customButton(String text, int width, String primaryColor, String secondaryColor) {
        Button button = new Button(text);
        if (width != 0) {
            button.setPrefWidth(width);
        }
        acceptColorTheme(button, DEFAULT_SIZE_FONT, primaryColor, secondaryColor);

        return button;
    }

    public static Button customButton(String text, int size, int fontSize) {
        return customButton(text, size, size, fontSize);
    }

    public static Button customButton(String text, int width, int height, int fontSize) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        acceptColorTheme(button, fontSize, AppTheme.BUTTON_PRIMARY, AppTheme.BUTTON_PRIMARY_HOVER);

        return button;
    }

    public static Button deleteToggleButton(int size) {
        Button button = new Button();
        button.setMinSize(size, size);
        button.setMaxSize(size, size);

        // Рисуем жирный минус через Utils
        StackPane icon = Utils.createAdjustIcon(false, size);
        button.setGraphic(icon);

        final boolean[] isActive = {false};

        // Цвета
        String colorNormal = AppTheme.BUTTON_PRIMARY; // Золотой
        String colorActive = AppTheme.BUTTON_DANGER;  // Красный
        String colorHover = AppTheme.BUTTON_PRIMARY_HOVER;

        // Общий каркас стиля (без цвета фона)
        String commonStyle = """
        -fx-background-radius: 4;
        -fx-border-radius: 4;
        -fx-border-width: 1;
        -fx-cursor: hand;
        -fx-padding: 0;
        """;

        // Функция для сборки стиля
        java.util.function.Function<String, String> styleBuilder = (color) ->
                commonStyle + "-fx-background-color: " + color + "; -fx-border-color: derive(" + color + ", -20%);";

        button.setStyle(styleBuilder.apply(colorNormal));

        button.setOnMouseEntered(e -> {
            if (!isActive[0]) button.setStyle(styleBuilder.apply(colorHover));
        });

        button.setOnMouseExited(e -> button.setStyle(styleBuilder.apply(isActive[0] ? colorActive : colorNormal)));

        button.setOnAction(e -> {
            isActive[0] = !isActive[0];
            button.setStyle(styleBuilder.apply(isActive[0] ? colorActive : colorHover));
            // Сохраняем состояние в UserData, чтобы удобно было читать снаружи
            button.setUserData(isActive[0]);
        });

        return button;
    }

    private static void acceptColorTheme(Button button, int fontSize, String primaryColor, String secondaryColor) {
        String common = """
                -fx-font-weight: bold;
                -fx-background-radius: 6;
                -fx-border-radius: 6;
                -fx-border-width: 1;
                -fx-border-color: transparent;
                -fx-font-size: %dpx;
                -fx-text-fill: %s;
                """.formatted(fontSize, AppTheme.BUTTON_TEXT);

        String base = common + "-fx-background-color: " + primaryColor + ";";
        String hover = common + "-fx-background-color: " + secondaryColor + ";";

        button.setStyle(base);
        button.setOnMouseEntered(e -> button.setStyle(hover));
        button.setOnMouseExited(e -> button.setStyle(base));
    }
}
