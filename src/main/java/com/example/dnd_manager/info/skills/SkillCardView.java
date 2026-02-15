package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.util.Duration;

public class SkillCardView extends VBox {

    private static final int CARD_WIDTH = 180;
    private static final int CARD_HEIGHT = 310;
    private static final int ICON_SIZE = 70;

    private final Popup customPopup = new Popup();
    private boolean isMouseInPopup = false;
    private boolean isMouseInDesc = false;

    private final Timeline glowTimeline = new Timeline();
    private final Timeline appearanceTimer = new Timeline();
    private final DropShadow glowEffect = new DropShadow(0, Color.color(0.78, 0.61, 0.24, 0.0));

    private final Label briefDesc = new Label();

    public SkillCardView(Skill skill, String characterName) {
        setSpacing(6);
        setPadding(new Insets(15, 10, 15, 10));
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        setMinSize(CARD_WIDTH, CARD_HEIGHT);
        setMaxWidth(CARD_WIDTH);

        glowEffect.setSpread(0.1);
        setEffect(glowEffect);

        setStyle("-fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1a1a1a);" +
                "-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-width: 1.5; -fx-border-color: #4a4a4a;");

        // --- 1. ICON ---
        ImageView icon = new ImageView();
        if (skill.iconPath() != null && !skill.iconPath().isBlank()) {
            icon.setImage(new Image(CharacterAssetResolver.resolve(characterName, skill.iconPath())));
        }
        icon.setFitWidth(ICON_SIZE);
        icon.setFitHeight(ICON_SIZE);
        StackPane iconFrame = new StackPane(icon);
        iconFrame.setMaxSize(ICON_SIZE + 4, ICON_SIZE + 4);
        iconFrame.setStyle("-fx-border-color: #c89b3c; -fx-border-width: 2; -fx-border-radius: 6; -fx-background-color: #1e1e1e;");

        // --- 2. NAME ---
        Label nameLabel = new Label(skill.name().toUpperCase());
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #c89b3c;");
        nameLabel.setWrapText(true);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMaxWidth(CARD_WIDTH - 20);
        nameLabel.setMinHeight(Region.USE_PREF_SIZE);

        // --- 3. EFFECTS (FIXED) ---
        FlowPane effectsPane = new FlowPane(5, 5);
        effectsPane.setAlignment(Pos.CENTER);

        // ВАЖНО: Указываем ширину, после которой FlowPane должен переносить элементы
        effectsPane.setPrefWrapLength(CARD_WIDTH - 20);
        effectsPane.setMaxWidth(CARD_WIDTH - 20);

        for (SkillEffect effect : skill.effects()) {
            // ВАЖНО: Берем DisplayName (он вернет кастомное имя или тип) + Значение
            Label eLabel = getLabel(effect);

            effectsPane.getChildren().add(eLabel);
        }

        // --- 4. DESCRIPTION (Trigger Area) ---
        Separator separator = new Separator();
        separator.setOpacity(0.2);
        VBox.setMargin(nameLabel, new Insets(5, 0, 5, 0));

        briefDesc.setText(skill.description());
        briefDesc.setStyle("-fx-font-size: 10px; -fx-text-fill: #888888; -fx-font-style: italic;");
        briefDesc.setWrapText(true);
        briefDesc.setTextAlignment(TextAlignment.CENTER);
        briefDesc.setCursor(javafx.scene.Cursor.HAND);
        VBox.setVgrow(briefDesc, Priority.ALWAYS);

        getChildren().addAll(iconFrame, nameLabel, effectsPane, separator, briefDesc);

        setupCustomPopup(skill);
        setupInteractions();
    }

    private Label getLabel(SkillEffect effect) {
        String labelText = effect.getDisplayName() + " " + effect.getValue();

        Label eLabel = new Label(labelText);
        String color = colorByEffect(effect.getType());

        eLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: white; -fx-padding: 3 6; " +
                "-fx-background-color: " + color + "33; -fx-background-radius: 4; " +
                "-fx-border-color: " + color + "; -fx-border-width: 1;");

        // Заставляем лейбл не быть шире карточки, если текст очень длинный
        eLabel.setMaxWidth(CARD_WIDTH - 25);
        eLabel.setWrapText(true); // Разрешаем перенос внутри самого лейбла, если одно слово супер длинное
        eLabel.setTextAlignment(TextAlignment.CENTER);
        return eLabel;
    }

    private void setupCustomPopup(Skill skill) {
        VBox popupContent = new VBox(0);
        popupContent.setPadding(new Insets(18)); // Чуть больше отступов для солидности

        // Стили: Глубокий графит и мягкое золото
        popupContent.setStyle(
                "-fx-background-color: #1a1a1a; " +
                        "-fx-border-color: #c89b3c; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10;"
        );

        popupContent.setPrefWidth(380); // Немного расширим для удобства чтения
        popupContent.setMaxWidth(380);

        Label fullDesc = new Label(skill.description());
        fullDesc.setStyle("-fx-font-size: 14px; -fx-text-fill: #dcdcdc; -fx-line-spacing: 3px;");
        fullDesc.setWrapText(true);
        fullDesc.setPrefWidth(340); // Ширина текста внутри (380 - 2*18 - запас)
        fullDesc.setMaxWidth(340);

        // ЛОГИКА: Проверяем длину текста
        // Если описание короткое (примерно до 400 символов), выводим просто Label
        if (skill.description().length() < 400) {
            popupContent.getChildren().add(fullDesc);
        } else {
            // Если текста много — используем ScrollPane
            ScrollPane scrollPane = AppScrollPaneFactory.defaultPane(fullDesc);
            scrollPane.setFitToWidth(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            // УСТАНОВКА ВЫСОТЫ: Чтобы не было "2 строк", ставим комфортную высоту для длинного текста
            scrollPane.setPrefHeight(600); // Теперь тут будет честных ~25-30 строк
            scrollPane.setMaxHeight(750); // Лимит, после которого пойдет прокрутка

            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-padding: 0;");

            popupContent.getChildren().add(scrollPane);
        }

        customPopup.getContent().clear();
        customPopup.getContent().add(popupContent);

        // Добавляем тень
        popupContent.setEffect(new javafx.scene.effect.DropShadow(25, Color.BLACK));

        popupContent.setOnMouseEntered(e -> isMouseInPopup = true);
        popupContent.setOnMouseExited(e -> {
            isMouseInPopup = false;
            checkAndClosePopup();
        });
    }

    private void setupInteractions() {
        appearanceTimer.getKeyFrames().add(new KeyFrame(Duration.millis(350), ae -> showPopup()));

        setOnMouseEntered(e -> animateGlow(true));
        setOnMouseExited(e -> animateGlow(false));

        briefDesc.setOnMouseEntered(e -> {
            isMouseInDesc = true;
            appearanceTimer.playFromStart();
        });

        briefDesc.setOnMouseExited(e -> {
            isMouseInDesc = false;
            appearanceTimer.stop();
            Timeline closeDelay = new Timeline(new KeyFrame(Duration.millis(150), ae -> checkAndClosePopup()));
            closeDelay.play();
        });
    }

    private void showPopup() {
        if (!isMouseInDesc || customPopup.isShowing()) return;

        double popupWidth = 380;
        double gap = 8;

        double x = briefDesc.localToScreen(briefDesc.getBoundsInLocal()).getMaxX() + gap;
        double y = briefDesc.localToScreen(briefDesc.getBoundsInLocal()).getMinY() - 80;

        if (x + popupWidth > Screen.getPrimary().getVisualBounds().getMaxX()) {
            x = this.localToScreen(this.getBoundsInLocal()).getMinX() - popupWidth - gap;
        }

        if (y < 20) {
            y = 20;
        }

        customPopup.show(this.getScene().getWindow(), x, y);
    }

    private void animateGlow(boolean show) {
        glowTimeline.stop();
        glowTimeline.getKeyFrames().clear();
        KeyValue kvRadius = new KeyValue(glowEffect.radiusProperty(), show ? 30.0 : 0.0);
        KeyValue kvColor = new KeyValue(glowEffect.colorProperty(),
                show ? Color.color(0.78, 0.61, 0.24, 0.7) : Color.color(0.78, 0.61, 0.24, 0.0));
        glowTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.4), kvRadius, kvColor));
        glowTimeline.play();
    }

    private void checkAndClosePopup() {
        if (!isMouseInDesc && !isMouseInPopup) {
            customPopup.hide();
        }
    }

    private String colorByEffect(String type) {
        if (type == null) return "#55ccff";
        return switch (type) {
            case "DAMAGE" -> "#ff5555";
            case "HEAL" -> "#55ff55";
            default -> "#55ccff";
        };
    }
}