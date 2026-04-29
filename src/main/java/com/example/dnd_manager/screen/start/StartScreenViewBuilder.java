package com.example.dnd_manager.screen.start;

import com.example.dnd_manager.info.version.AppInfo;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.updater.model.GitHubRelease;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Builds StartScreen JavaFX view.
 */
public class StartScreenViewBuilder {

    private static final int MAIN_ACTION_HEIGHT = 52;
    private static final int SECONDARY_ACTION_HEIGHT = 44;
    private static final int UPDATE_ACTION_WIDTH = 250;
    private static final int UTILITY_ACTION_HEIGHT = 42;
    private static final int CONTACT_BUTTON_FONT = 13;
    private static final int TELEGRAM_BUTTON_HEIGHT = 42;
    private static final int HERO_CONTENT_WIDTH = 760;
    private static final int INFO_CARD_MIN_WIDTH = 320;
    private static final int INFO_CARD_PREF_WIDTH = 380;
    private static final int CONTACT_CARD_MIN_WIDTH = 280;
    private static final int CONTACT_CARD_PREF_WIDTH = 320;
    private static final int INFO_NEWS_MIN_HEIGHT = 270;
    private static final int ACTION_PANEL_SPACING = 14;
    private static final int ACTION_PANEL_PADDING = 28;
    private static final int ACTION_PANEL_PADDING_NARROW = 18;
    private static final int ACTION_PANEL_MIN_WIDTH = 360;
    private static final int ACTION_PANEL_MIN_WIDTH_NARROW = 320;
    private static final int ACTION_PANEL_PREF_WIDTH = 440;
    private static final int ACTION_PANEL_PREF_WIDTH_NARROW = 350;
    private static final int ACTION_PANEL_MAX_WIDTH = 460;
    private static final int ACTION_PANEL_MAX_WIDTH_NARROW = 380;
    private static final int ICON_BADGE_SIZE = 38;
    private static final int IMAGE_ICON_SIZE = 22;
    private static final double WHITE_ICON_BRIGHTNESS = 1.0;
    private static final double WHITE_ICON_CONTRAST = 1.0;
    private static final double WHITE_ICON_SATURATION = -1.0;
    private static final int HERO_PANEL_SPACING_NORMAL = 22;
    private static final int HERO_PANEL_SPACING_ADAPTIVE = 14;
    private static final int PADDING_NORMAL = 24;
    private static final int PADDING_NARROW = 14;
    private static final double SCREEN_WIDTH_BREAKPOINT = 1320.0;
    private static final int NEWS_MIN_HEIGHT_NARROW = 220;
    private static final int NEWS_PREF_HEIGHT = 290;
    private static final int NEWS_PREF_HEIGHT_NARROW = 220;
    private static final int INFO_CARD_MIN_WIDTH_NARROW = 260;
    private static final int CONTACT_CARD_MIN_WIDTH_NARROW = 230;
    private static final int ACTION_STRIP_GAP = 10;

    private final StartScreenStyleProvider styles;
    private final Supplier<List<GitHubRelease>> releaseSupplier;
    private VBox newsPanel;
    private VBox contactPanel;

    public StartScreenViewBuilder() {
        this(List::of);
    }

    public StartScreenViewBuilder(Supplier<List<GitHubRelease>> releaseSupplier) {
        this(new StartScreenStyleProvider(), releaseSupplier);
    }

    StartScreenViewBuilder(StartScreenStyleProvider styles, Supplier<List<GitHubRelease>> releaseSupplier) {
        this.styles = Objects.requireNonNull(styles, "styles must not be null");
        this.releaseSupplier = Objects.requireNonNull(releaseSupplier, "releaseSupplier must not be null");
    }

    public Parent build(StartScreenViewActions actions) {
        Objects.requireNonNull(actions, "actions must not be null");

        BorderPane root = new BorderPane();
        root.setMinHeight(620);
        root.setStyle(styles.rootStyle());

        HBox shell = new HBox(HERO_PANEL_SPACING_NORMAL);
        shell.setAlignment(Pos.CENTER_LEFT);
        shell.setPadding(new Insets(PADDING_NORMAL, 28, PADDING_NORMAL, 28));
        shell.setMaxWidth(Double.MAX_VALUE);

        StackPane heroPanel = buildHeroPanel(actions);
        VBox actionPanel = buildActionPanel(actions);

        HBox.setHgrow(heroPanel, Priority.ALWAYS);
        shell.getChildren().addAll(heroPanel, actionPanel);

        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) {
                return;
            }
            applyAdaptiveLayout(newScene.widthProperty().doubleValue(), shell, actionPanel);
            newScene.widthProperty().addListener((wObs, oldW, newW) ->
                    applyAdaptiveLayout(newW.doubleValue(), shell, actionPanel)
            );
        });

        root.setCenter(shell);
        root.setBottom(buildFooter());
        return root;
    }

    private StackPane buildHeroPanel(StartScreenViewActions actions) {
        StackPane heroPanel = new StackPane();
        heroPanel.setMinWidth(360);
        heroPanel.setMaxWidth(Double.MAX_VALUE);
        heroPanel.setAlignment(Pos.CENTER_LEFT);
        heroPanel.setStyle(styles.heroPanelStyle());

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(34, 32, 28, 32));
        content.setMaxWidth(Double.MAX_VALUE);

        VBox introBlock = new VBox(15);
        introBlock.setAlignment(Pos.CENTER_LEFT);
        introBlock.setMaxWidth(HERO_CONTENT_WIDTH);

        Label eyebrow = new Label(I18n.t("title.startEyebrow"));
        eyebrow.setStyle(styles.eyebrowStyle());

        Label title = new Label(I18n.t("title.start"));
        title.setWrapText(true);
        title.setStyle(styles.titleStyle());

        Label subtitle = new Label(I18n.t("title.startSubtitle"));
        subtitle.setWrapText(true);
        subtitle.setMaxWidth(HERO_CONTENT_WIDTH);
        subtitle.setStyle(styles.subtitleStyle());

        Region accentRail = new Region();
        accentRail.setPrefSize(180, 3);
        accentRail.setMaxSize(180, 3);
        accentRail.setStyle(styles.accentRailStyle());

        introBlock.getChildren().addAll(eyebrow, title, accentRail, subtitle);

        HBox infoCluster = buildInfoCluster(actions);
        VBox.setVgrow(infoCluster, Priority.ALWAYS);

        Label quote = new Label(I18n.t("title.startQuote"));
        quote.setWrapText(true);
        quote.setMaxWidth(HERO_CONTENT_WIDTH);
        quote.setStyle(styles.sectionHintStyle());

        Region footerSpacer = new Region();
        VBox.setVgrow(footerSpacer, Priority.ALWAYS);

        content.getChildren().addAll(introBlock, quote, footerSpacer, infoCluster);
        playEntrance(content);
        heroPanel.getChildren().add(content);
        return heroPanel;
    }

    private HBox buildInfoCluster(StartScreenViewActions actions) {
        HBox cardsRow = new HBox(16);
        cardsRow.setAlignment(Pos.BOTTOM_LEFT);
        cardsRow.setMaxWidth(Double.MAX_VALUE);

        StartScreenNewsPanel newsPanel = buildNewsPanel();
        this.newsPanel = newsPanel;
        newsPanel.setMinWidth(INFO_CARD_MIN_WIDTH);
        newsPanel.setPrefWidth(INFO_CARD_PREF_WIDTH);
        newsPanel.setMaxWidth(460);
        newsPanel.setMinHeight(INFO_NEWS_MIN_HEIGHT);
        newsPanel.setPrefHeight(NEWS_PREF_HEIGHT);
        newsPanel.setStyle(styles.mapNodeStyle());
        VBox.setVgrow(newsPanel, Priority.NEVER);

        VBox contactPanel = buildContactCard(actions);
        this.contactPanel = contactPanel;
        contactPanel.setMinWidth(CONTACT_CARD_MIN_WIDTH);
        contactPanel.setPrefWidth(CONTACT_CARD_PREF_WIDTH);
        contactPanel.setMaxWidth(360);
        contactPanel.setMinHeight(INFO_NEWS_MIN_HEIGHT);
        contactPanel.setPrefHeight(NEWS_PREF_HEIGHT);
        contactPanel.setStyle(styles.mapNodeStyle());

        HBox.setHgrow(newsPanel, Priority.ALWAYS);
        HBox.setHgrow(contactPanel, Priority.NEVER);
        cardsRow.getChildren().addAll(newsPanel, contactPanel);
        return cardsRow;
    }

    private VBox buildActionPanel(StartScreenViewActions actions) {
        VBox panel = new VBox(ACTION_PANEL_SPACING);
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPrefWidth(ACTION_PANEL_PREF_WIDTH);
        panel.setMinWidth(ACTION_PANEL_MIN_WIDTH);
        panel.setMaxWidth(ACTION_PANEL_MAX_WIDTH);
        panel.setPadding(new Insets(ACTION_PANEL_PADDING));
        panel.setStyle(styles.actionPanelStyle());

        VBox header = new VBox(6);
        header.setPadding(new Insets(14, 16, 14, 16));
        header.setStyle(styles.actionHeaderStyle());

        Label title = new Label(I18n.t("title.quickActions"));
        title.setStyle(styles.sectionTitleStyle());

        Label hint = new Label(I18n.t("title.quickActionsHint"));
        hint.setWrapText(true);
        hint.setStyle(styles.sectionHintStyle());
        header.getChildren().addAll(title, hint);

        VBox mainActions = new VBox(ACTION_STRIP_GAP);
        mainActions.setAlignment(Pos.CENTER);

        Button createButton = createPrimaryButton(I18n.t("button.create"), actions.onCreate());
        Button loadButton = createSecondaryButton(I18n.t("button.load"), actions.onLoad(), "load");
        Button editButton = createSecondaryButton(I18n.t("button.edit"), actions.onEdit(), "pencil");
        mainActions.getChildren().addAll(createButton, loadButton, editButton);

        VBox secondaryActions = new VBox(ACTION_STRIP_GAP);
        secondaryActions.setAlignment(Pos.CENTER);
        Button assetManagerButton = createSecondaryButton(
                I18n.t("button.assets"),
                actions.onAssets(),
                "asset"
        );
        Button transferButton = createSecondaryButton(
                I18n.t("button.importExport"),
                actions.onTransfer(),
                "arrows"
        );
        secondaryActions.getChildren().addAll(assetManagerButton, transferButton);

        VBox utilityActions = new VBox(ACTION_STRIP_GAP);
        utilityActions.setAlignment(Pos.CENTER);

        Button languageButton = createUtilityButton(I18n.t("button.language"), actions.onLanguageChange(), "globe");
        Button updateButton = createUtilityButton(I18n.t("button.checkUpdate"), null, "update");
        updateButton.setPrefWidth(UPDATE_ACTION_WIDTH);
        updateButton.setOnAction(e -> actions.onUpdateCheck().accept(updateButton));
        Button donateButton = createDonateButton(I18n.t("button.donate"), actions.onDonate());

        HBox smallActions = new HBox(ACTION_STRIP_GAP, languageButton, donateButton);
        smallActions.setAlignment(Pos.CENTER);
        HBox.setHgrow(languageButton, Priority.ALWAYS);
        HBox.setHgrow(donateButton, Priority.ALWAYS);
        utilityActions.getChildren().addAll(updateButton, smallActions);

        Region bottomFiller = new Region();
        VBox.setVgrow(bottomFiller, Priority.ALWAYS);

        panel.getChildren().addAll(header, mainActions, secondaryActions, bottomFiller, utilityActions);
        return panel;
    }

    private VBox buildContactCard(StartScreenViewActions actions) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(16));

        Label title = new Label(I18n.t("telegram.title"));
        title.setStyle(styles.newsCaptionStyle());

        Label description = new Label(I18n.t("telegram.developerText"));
        description.setWrapText(true);
        description.setStyle(styles.newsSummaryStyle());

        Button developerButton = createTelegramButton(
                I18n.t("telegram.developerButton"),
                actions.onDeveloperTelegram(),
                "info"
        );
        Button botButton = createTelegramButton(
                I18n.t("telegram.botButton"),
                actions.onCommunityBot(),
                "telegram"
        );

        VBox links = new VBox(8);
        links.setAlignment(Pos.CENTER);
        links.getChildren().addAll(developerButton, botButton);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(title, description, spacer, links);
        return card;
    }

    private Button createPrimaryButton(String text, Runnable action) {
        Button button = createButton(text, MAIN_ACTION_HEIGHT, action);
        applyButtonIcon(button, "sword");
        styles.applyPrimaryAction(button);
        button.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(button, Priority.NEVER);
        return button;
    }

    private Button createSecondaryButton(String text, Runnable action, String icon) {
        Button button = createButton(text, SECONDARY_ACTION_HEIGHT, action);
        applyButtonIcon(button, icon);
        styles.applySecondaryAction(button);
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Button createUtilityButton(String text, Runnable action, String icon) {
        Button button = createButton(text, UTILITY_ACTION_HEIGHT, action);
        applyButtonIcon(button, icon);
        styles.applyUtilityAction(button);
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Button createDonateButton(String text, Runnable action) {
        Button button = createButton(text, UTILITY_ACTION_HEIGHT, action);
        applyButtonIcon(button, "heart");
        styles.applyDonateAction(button);
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Button createTelegramButton(String text, Runnable action, String icon) {
        Button button = createButton(text, TELEGRAM_BUTTON_HEIGHT, action);
        applyButtonIcon(button, icon);
        styles.applyTelegramAction(button, CONTACT_BUTTON_FONT);
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Button createButton(String text, int height, Runnable action) {
        Button button = new Button(text);
        button.setMnemonicParsing(false);
        button.setContentDisplay(ContentDisplay.LEFT);
        button.setGraphicTextGap(14);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setTextOverrun(OverrunStyle.ELLIPSIS);
        button.setPrefHeight(height);
        button.setMinHeight(height);
        button.setMaxHeight(height);
        button.setMaxWidth(Double.MAX_VALUE);
        if (action != null) {
            button.setOnAction(e -> action.run());
        }
        return button;
    }

    private void applyButtonIcon(Button button, String icon) {
        button.setGraphic(createIcon(icon));
    }

    private StackPane createIcon(String icon) {
        switch (icon) {
            case "globe" -> {
                return createGlobeIcon();
            }
            case "sword" -> {
                return createImageIcon("/com/example/dnd_manager/icon/sword_fight.png", true);
            }
            case "info" -> {
                return createInfoIcon();
            }
            case "telegram" -> {
                return createTelegramIcon();
            }
            case "pencil" -> {
                return createPencilIcon();
            }
            case "asset" -> {
                return createAssetIcon();
            }
            case "update" -> {
                return createImageIcon("/com/example/dnd_manager/icon/update_icon.png", true);
            }
            case "heart" -> {
                return createGlyphIcon("♥", 24);
            }
            default -> {
                return createGlyphIcon(iconGlyph(icon), 18);
            }
        }
    }

    private StackPane createGlyphIcon(String glyphText, int fontSize) {
        StackPane root = new StackPane();
        root.setMinSize(ICON_BADGE_SIZE, ICON_BADGE_SIZE);
        root.setPrefSize(ICON_BADGE_SIZE, ICON_BADGE_SIZE);
        root.setMaxSize(ICON_BADGE_SIZE, ICON_BADGE_SIZE);
        root.setStyle(styles.iconBadgeStyle());
        Label glyph = new Label(glyphText);
        glyph.setAlignment(Pos.CENTER);
        glyph.setStyle(styles.iconGlyphStyle(fontSize));
        root.getChildren().add(glyph);
        return root;
    }

    private StackPane createImageIcon(String resourcePath, boolean forceWhite) {
        StackPane root = new StackPane();
        root.setMinSize(ICON_BADGE_SIZE, ICON_BADGE_SIZE);
        root.setPrefSize(ICON_BADGE_SIZE, ICON_BADGE_SIZE);
        root.setMaxSize(ICON_BADGE_SIZE, ICON_BADGE_SIZE);
        root.setStyle(styles.iconBadgeStyle());

        ImageView icon = new ImageView(new Image(Objects.requireNonNull(
                getClass().getResource(resourcePath),
                "Icon resource must exist: " + resourcePath
        ).toExternalForm()));
        icon.setFitWidth(IMAGE_ICON_SIZE);
        icon.setFitHeight(IMAGE_ICON_SIZE);
        icon.setPreserveRatio(true);
        icon.setSmooth(true);
        if (forceWhite) {
            ColorAdjust whiteIcon = new ColorAdjust();
            whiteIcon.setSaturation(WHITE_ICON_SATURATION);
            whiteIcon.setBrightness(WHITE_ICON_BRIGHTNESS);
            whiteIcon.setContrast(WHITE_ICON_CONTRAST);
            icon.setEffect(whiteIcon);
        }
        root.getChildren().add(icon);
        return root;
    }

    private StackPane createGlobeIcon() {
        StackPane root = new StackPane();
        root.setMinSize(38, 38);
        root.setPrefSize(38, 38);
        root.setMaxSize(38, 38);
        root.setStyle(styles.iconBadgeStyle());

        Circle outline = new Circle(0, 0, 9);
        Ellipse meridian = new Ellipse(0, 0, 4.5, 9);
        Line equator = new Line(-8.5, 0, 8.5, 0);
        Line upperStripe = new Line(-7.2, -4, 7.2, -4);
        Line lowerStripe = new Line(-7.2, 4, 7.2, 4);
        Shape[] parts = {outline, meridian, equator, upperStripe, lowerStripe};
        for (Shape part : parts) {
            part.setStyle(styles.iconShapeStyle());
        }

        root.getChildren().add(new Group(parts));
        return root;
    }

    private StackPane createInfoIcon() {
        StackPane root = new StackPane();
        root.setMinSize(38, 38);
        root.setPrefSize(38, 38);
        root.setMaxSize(38, 38);
        root.setStyle(styles.iconBadgeStyle());

        Circle outline = new Circle(0, 0, 9);
        outline.setStyle(styles.iconShapeStyle());
        Label glyph = new Label("i");
        glyph.setAlignment(Pos.CENTER);
        glyph.setStyle(styles.iconGlyphStyle(17));

        root.getChildren().addAll(new Group(outline), glyph);
        return root;
    }

    private StackPane createTelegramIcon() {
        StackPane root = new StackPane();
        root.setMinSize(38, 38);
        root.setPrefSize(38, 38);
        root.setMaxSize(38, 38);
        root.setStyle(styles.iconBadgeStyle());

        Polygon plane = new Polygon(
                -10.0, -2.0,
                10.0, -10.0,
                4.0, 10.0,
                -1.0, 3.0,
                -7.0, 8.0
        );
        plane.setStyle(styles.telegramPlaneStyle());
        root.getChildren().add(plane);
        return root;
    }

    private StackPane createPencilIcon() {
        StackPane root = new StackPane();
        root.setMinSize(38, 38);
        root.setPrefSize(38, 38);
        root.setMaxSize(38, 38);
        root.setStyle(styles.iconBadgeStyle());

        Rectangle body = new Rectangle(-3, -10, 6, 17);
        body.setArcWidth(2);
        body.setArcHeight(2);
        body.setStyle(styles.filledIconShapeStyle());

        Polygon tip = new Polygon(-3.0, 7.0, 3.0, 7.0, 0.0, 12.0);
        tip.setStyle(styles.filledIconShapeStyle());

        Group pencil = new Group(body, tip);
        pencil.setRotate(42);
        root.getChildren().add(pencil);
        return root;
    }

    private StackPane createAssetIcon() {
        StackPane root = new StackPane();
        root.setMinSize(38, 38);
        root.setPrefSize(38, 38);
        root.setMaxSize(38, 38);
        root.setStyle(styles.iconBadgeStyle());

        Rectangle frame = new Rectangle(-10, -8, 20, 16);
        frame.setArcWidth(3);
        frame.setArcHeight(3);
        frame.setStyle(styles.iconShapeStyle());

        Circle sun = new Circle(4, -3, 2.1);
        sun.setStyle(styles.filledIconShapeStyle());

        Polygon mountain = new Polygon(-8.0, 7.0, -2.0, 1.0, 2.0, 5.0, 5.0, 2.0, 9.0, 7.0);
        mountain.setStyle(styles.iconShapeStyle());

        root.getChildren().addAll(new Group(frame, sun, mountain));
        return root;
    }

    private String iconGlyph(String icon) {
        return switch (icon) {
            case "sword" -> "⚔";
            case "load" -> "▤";
            case "arrows" -> "⇄";
            case "heart" -> "♥";
            case "scroll" -> "☰";
            case "dice" -> "⬡";
            default -> "✦";
        };
    }

    private StartScreenNewsPanel buildNewsPanel() {
        return new StartScreenNewsPanel(styles, releaseSupplier);
    }

    private void playEntrance(Node node) {
        node.setOpacity(0.0);
        node.setTranslateY(8);

        FadeTransition fade = new FadeTransition(Duration.millis(420), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(420), node);
        slide.setFromY(8);
        slide.setToY(0);

        fade.play();
        slide.play();
    }

    private HBox buildFooter() {
        Label statusLabel = new Label(I18n.t("title.release"));
        statusLabel.setStyle(styles.footerTextStyle());

        Label versionLabel = new Label("v" + AppInfo.getVersion());
        versionLabel.setStyle(styles.footerTextStyle());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox footer = new HBox(12, statusLabel, spacer, versionLabel);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(9, 16, 9, 16));
        footer.setStyle(styles.footerStyle());
        return footer;
    }

    private void applyAdaptiveLayout(double width, HBox shell, VBox actionPanel) {
        boolean compactMode = width < SCREEN_WIDTH_BREAKPOINT;

        if (compactMode) {
            shell.setSpacing(HERO_PANEL_SPACING_ADAPTIVE);
            shell.setPadding(new Insets(PADDING_NARROW, PADDING_NARROW, PADDING_NARROW, PADDING_NARROW));
            actionPanel.setPadding(new Insets(ACTION_PANEL_PADDING_NARROW));
            actionPanel.setMinWidth(ACTION_PANEL_MIN_WIDTH_NARROW);
            actionPanel.setPrefWidth(ACTION_PANEL_PREF_WIDTH_NARROW);
            actionPanel.setMaxWidth(ACTION_PANEL_MAX_WIDTH_NARROW);

            if (newsPanel != null) {
                newsPanel.setMinWidth(INFO_CARD_MIN_WIDTH_NARROW);
                newsPanel.setMinHeight(NEWS_MIN_HEIGHT_NARROW);
                newsPanel.setPrefHeight(NEWS_PREF_HEIGHT_NARROW);
            }

            if (contactPanel != null) {
                contactPanel.setMinWidth(CONTACT_CARD_MIN_WIDTH_NARROW);
                contactPanel.setMinHeight(NEWS_MIN_HEIGHT_NARROW);
                contactPanel.setPrefHeight(NEWS_PREF_HEIGHT_NARROW);
            }
        } else {
            shell.setSpacing(HERO_PANEL_SPACING_NORMAL);
            shell.setPadding(new Insets(PADDING_NORMAL, 28, PADDING_NORMAL, 28));
            actionPanel.setPadding(new Insets(ACTION_PANEL_PADDING));
            actionPanel.setMinWidth(ACTION_PANEL_MIN_WIDTH);
            actionPanel.setPrefWidth(ACTION_PANEL_PREF_WIDTH);
            actionPanel.setMaxWidth(ACTION_PANEL_MAX_WIDTH);

            if (newsPanel != null) {
                newsPanel.setMinWidth(INFO_CARD_MIN_WIDTH);
                newsPanel.setMinHeight(INFO_NEWS_MIN_HEIGHT);
                newsPanel.setPrefHeight(NEWS_PREF_HEIGHT);
            }

            if (contactPanel != null) {
                contactPanel.setMinWidth(CONTACT_CARD_MIN_WIDTH);
                contactPanel.setMinHeight(INFO_NEWS_MIN_HEIGHT);
                contactPanel.setPrefHeight(NEWS_PREF_HEIGHT);
            }
        }
    }
}
