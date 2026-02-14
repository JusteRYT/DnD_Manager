package com.example.dnd_manager.theme;

import javafx.scene.control.Button;

/**
 * Factory for creating styled application buttons.
 */
public final class AppButtonFactory {

    private static final int DEFAULT_SIZE_FONT = 14;
    private static final String HUD_GOLD = "#c89b3c";
    private static final String HUD_BG = "#2b2b2b";
    private static final String HUD_BG_HOVER = "#3c3c3c";

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

    public static Button hudIconButton(int size) {
        Button button = new Button();

        // Полная фиксация размеров для предотвращения "дрыганья"
        button.setMinWidth(size);
        button.setMaxWidth(size);
        button.setMinHeight(size);
        button.setMaxHeight(size);

        // Используем цвета из AppTheme
        String bg = AppTheme.BUTTON_PRIMARY;
        String bgHover = "#3a3a3a"; // Чуть светлее вторичного фона
        String accent = AppTheme.BORDER_ACCENT;

        // Базовый шаблон стиля. Все размеры (border, padding) зафиксированы.
        String layoutTemplate = """
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-border-width: 2;
            -fx-cursor: hand;
            -fx-padding: 0;
            """;

        String baseStyle = layoutTemplate + """
            -fx-background-color: %s;
            -fx-border-color: %s;
            """.formatted(bg, accent);

        String hoverStyle = layoutTemplate + """
            -fx-background-color: %s;
            -fx-border-color: %s;
            -fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.4), 8, 0, 0, 0);
            """.formatted(bgHover, AppTheme.BUTTON_PRIMARY_HOVER);

        String pressedStyle = layoutTemplate + """
            -fx-background-color: #1a1a1a;
            -fx-border-color: #ffffff;
            -fx-scale-x: 0.95;
            -fx-scale-y: 0.95;
            """;

        button.setStyle(baseStyle);

        // Переключение стилей без пересчета размеров
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        button.setOnMousePressed(e -> button.setStyle(pressedStyle));
        button.setOnMouseReleased(e -> button.setStyle(button.isHover() ? hoverStyle : baseStyle));

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

    public static Button deleteToggleButton(String text, int width) {
        Button button = new Button(text);
        button.setPrefWidth(width);

        final boolean[] active = {false};

        // Цвета для кнопки
        String normal = AppTheme.BUTTON_PRIMARY;
        String activeColor = AppTheme.BUTTON_DANGER;
        String hoverColor = AppTheme.BUTTON_PRIMARY_HOVER;

        // Установим начальный цвет
        button.setStyle(baseStyle(normal));

        // Hover
        button.setOnMouseEntered(e -> {
            if (!active[0]) {
                button.setStyle(baseStyle(hoverColor));
            }
        });

        button.setOnMouseExited(e -> {
            if (!active[0]) {
                button.setStyle(baseStyle(normal));
            }
        });

        // Toggle при клике
        button.setOnAction(e -> {
            active[0] = !active[0];
            button.setStyle(baseStyle(active[0] ? activeColor : normal));
        });

        return button;
    }

    private static String baseStyle(String bgColor) {
        return "-fx-background-color: " + bgColor + ";" +
                "-fx-text-fill: " + AppTheme.BUTTON_TEXT + ";" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 6;";
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
