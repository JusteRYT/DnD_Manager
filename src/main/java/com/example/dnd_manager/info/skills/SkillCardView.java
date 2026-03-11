package com.example.dnd_manager.info.skills;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.util.Duration;

public class SkillCardView extends VBox {

    private static final int CARD_WIDTH = 180;
    private static final int CARD_HEIGHT = 310;
    private static final int ICON_SIZE = 70;

    private static final String ACCENT_COLOR = "#c89b3c";
    private static final String IDLE_BORDER = "#4a4a4a";

    private static final String BASE_STYLE = """
            -fx-background-color: linear-gradient(to bottom right, #2b2b2b, #1a1a1a);
            -fx-background-radius: 12;
            -fx-border-radius: 12;
            -fx-border-width: 1.5;
            -fx-padding: 15 10 15 10;
            """;

    private static final String IDLE_STYLE = BASE_STYLE + "-fx-border-color: " + IDLE_BORDER + ";";
    private static final String HOVER_STYLE = BASE_STYLE +
            "-fx-border-color: " + ACCENT_COLOR + ";" +
            "-fx-effect: dropshadow(three-pass-box, rgba(200, 155, 60, 0.4), 20, 0.1, 0, 0);";

    private final Popup customPopup = new Popup();
    private boolean isMouseInPopup = false;
    private boolean isMouseInDesc = false;
    private final Timeline appearanceTimer = new Timeline();
    private final Label briefDesc = new Label();
    private final InventoryItem sourceItem;

    public SkillCardView(Skill skill, Character character, InventoryItem sourceItem) {
        this.sourceItem = sourceItem;
        setSpacing(6);
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        setMinSize(CARD_WIDTH, CARD_HEIGHT);
        setMaxWidth(CARD_WIDTH);

        // Устанавливаем начальный вид
        setStyle(IDLE_STYLE);

        // --- 1. ICON ---
        ImageView icon = new ImageView();
        icon.setImage(CharacterAssetResolver.getImage(character, skill.iconPath(), ICON_SIZE, ICON_SIZE));
        icon.setFitWidth(ICON_SIZE);
        icon.setFitHeight(ICON_SIZE);

        StackPane iconFrame = new StackPane(icon);
        iconFrame.setMaxSize(ICON_SIZE + 4, ICON_SIZE + 4);
        iconFrame.setStyle("""
                    -fx-border-color: #c89b3c;
                    -fx-border-width: 2;
                    -fx-border-radius: 6;
                    -fx-background-color: #1e1e1e;
                    -fx-background-radius: 6;
                """);

        DropShadow frameGlow = new DropShadow();
        frameGlow.setBlurType(BlurType.THREE_PASS_BOX);
        frameGlow.setColor(Color.web("#c89b3c", 0.7));
        frameGlow.setRadius(12);
        frameGlow.setSpread(0.1);
        iconFrame.setEffect(frameGlow);

        Label sourceBadge = getSourceBadge(sourceItem);
        StackPane.setAlignment(sourceBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(sourceBadge, new Insets(-8, -8, 0, 0));
        iconFrame.getChildren().add(sourceBadge);

        // --- 2. NAME ---
        Label nameLabel = new Label(skill.name().toUpperCase());
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #c89b3c;");
        nameLabel.setWrapText(true);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setMaxWidth(CARD_WIDTH - 20);

        // --- 3. EFFECTS ---
        FlowPane effectsPane = new FlowPane(5, 5);
        effectsPane.setAlignment(Pos.CENTER);
        effectsPane.setPrefWrapLength(CARD_WIDTH - 20);

        for (SkillEffect effect : skill.effects()) {
            effectsPane.getChildren().add(getLabel(effect));
        }

        // --- 4. DESCRIPTION ---
        Separator separator = new Separator();
        separator.setOpacity(0.2);
        VBox.setMargin(nameLabel, new Insets(5, 0, 5, 0));

        String fulltext = I18n.t("skill.attrActivation") + ": " + skill.activationType() + "\n" + skill.description();
        briefDesc.setText(fulltext);
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
        String labelText = String.format("%s %s", effect.getDisplayName().toUpperCase(), effect.getValue());
        Label eLabel = new Label(labelText);

        String baseColor = colorByEffect(effect.getType());

        eLabel.setStyle(String.format("""
                    -fx-background-color: %1$s26;
                    -fx-border-color: %1$s80;
                    -fx-border-width: 1;
                    -fx-border-radius: 10;
                    -fx-background-radius: 10;
                    -fx-text-fill: white;
                    -fx-font-size: 10px;
                    -fx-font-weight: bold;
                    -fx-padding: 3 10 3 10;
                    -fx-letter-spacing: 0.5px;
                """, baseColor));

        DropShadow externalGlow = new DropShadow();
        externalGlow.setBlurType(BlurType.THREE_PASS_BOX);
        externalGlow.setRadius(8);
        externalGlow.setSpread(0.15);
        externalGlow.setColor(Color.web(baseColor, 0.5));

        eLabel.setEffect(externalGlow);

        // Constraint management
        eLabel.setMaxWidth(CARD_WIDTH - 25);
        eLabel.setWrapText(true);
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

        if (sourceItem != null) {
            Label itemSourceInfo = new Label(I18n.t("skill.provided_by") + ": " + sourceItem.getName());
            itemSourceInfo.setStyle("-fx-text-fill: #55ccff; -fx-font-size: 11px; -fx-font-weight: bold;");
            VBox.setMargin(itemSourceInfo, new Insets(0, 0, 10, 0));
            popupContent.getChildren().addFirst(itemSourceInfo);
        }
    }

    private void setupInteractions() {
        appearanceTimer.getKeyFrames().add(new KeyFrame(Duration.millis(350), ae -> showPopup()));

        setOnMouseEntered(e -> setStyle(HOVER_STYLE));
        setOnMouseExited(e -> setStyle(IDLE_STYLE));

        briefDesc.setOnMouseEntered(e -> {
            isMouseInDesc = true;
            appearanceTimer.playFromStart();
        });

        briefDesc.setOnMouseExited(e -> {
            isMouseInDesc = false;
            appearanceTimer.stop();
            new Timeline(new KeyFrame(Duration.millis(150), ae -> checkAndClosePopup())).play();
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

    /**
     * Creates a circular source indicator.
     * If from an item, it highlights with a glow. If innate, stays subtle.
     *
     * @param sourceItem The providing item or null.
     * @return A circular styled Label.
     */
    private Label getSourceBadge(InventoryItem sourceItem) {
        boolean isFromItem = sourceItem != null;

        String iconText = isFromItem ? "📦" : "\uD83D\uDC64";
        String bgColor = isFromItem ? "#55ccff" : "#4a4a4a";
        String textColor = isFromItem ? "#1a1a1a" : "#c89b3c";

        Label label = new Label(iconText);

        // Fixed size for perfect circle
        double size = 22;
        label.setMinSize(size, size);
        label.setMaxSize(size, size);
        label.setPrefSize(size, size);
        label.setAlignment(Pos.CENTER);

        label.setStyle(String.format("""
                    -fx-background-color: %1$s;
                    -fx-text-fill: %2$s;
                    -fx-font-size: 11px;
                    -fx-font-weight: bold;
                    -fx-background-radius: 50;
                    -fx-border-color: #1a1a1a;
                    -fx-border-width: 1.5;
                    -fx-border-radius: 50;
                """, bgColor, textColor));

        Tooltip tooltip = new Tooltip(
                isFromItem ? sourceItem.getName() : I18n.t("skill.source.innate")
        );
        tooltip.setShowDelay(Duration.millis(100));
        label.setTooltip(tooltip);

        DropShadow glow = new DropShadow();
        glow.setBlurType(BlurType.THREE_PASS_BOX);
        glow.setRadius(10);
        glow.setSpread(0.3);

        if (isFromItem) {
            glow.setColor(Color.web(bgColor, 0.8));
        } else {
            glow.setColor(Color.web("#c89b3c", 0.6));
        }
        label.setEffect(glow);

        return label;
    }
}