package com.example.dnd_manager.screen.start;

import javafx.scene.control.Button;

/**
 * Visual system for the start screen.
 */
public class StartScreenStyleProvider {

    private static final String ASTRAL_TEXT = "#e9edf3";
    private static final String DONATE_WINE = "#3c2336";

    public String rootStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 18% 24%, radius 58%, rgba(23, 35, 58, 0.86), transparent 62%),
                    radial-gradient(center 76% 14%, radius 64%, rgba(42, 36, 69, 0.74), transparent 66%),
                    radial-gradient(center 52% 82%, radius 72%, rgba(24, 37, 59, 0.78), transparent 62%),
                    linear-gradient(from 0% 0% to 100% 100%, #070b14, #11172a 48%, #151229 78%, #070b14);
                """;
    }

    public String heroPanelStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 53% 50%, radius 48%, rgba(175, 196, 216, 0.10), transparent 64%),
                    radial-gradient(center 80% 18%, radius 40%, rgba(196, 189, 214, 0.10), transparent 62%),
                    linear-gradient(from 0% 0% to 100% 100%, rgba(18, 26, 48, 0.92), rgba(13, 16, 30, 0.90));
                -fx-background-radius: 18;
                -fx-background-insets: 0;
                -fx-border-color: rgba(75, 93, 127, 0.58);
                -fx-border-radius: 18;
                -fx-border-width: 1;
                -fx-effect: innershadow(gaussian, rgba(223, 230, 236, 0.06), 32, 0.20, 0, 0);
                """;
    }

    public String actionPanelStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 0%, radius 78%, rgba(175, 196, 216, 0.08), transparent 64%),
                    linear-gradient(from 0% 0% to 100% 100%, rgba(17, 23, 41, 0.96), rgba(20, 17, 36, 0.94));
                -fx-background-radius: 18;
                -fx-border-color: rgba(72, 85, 117, 0.82);
                -fx-border-radius: 18;
                -fx-border-width: 1;
                -fx-effect:
                    dropshadow(gaussian, rgba(0, 0, 0, 0.36), 28, 0.22, 0, 12),
                    innershadow(gaussian, rgba(223, 230, 236, 0.05), 24, 0.16, 0, 0);
                """;
    }

    public String accentRailStyle() {
        return """
                -fx-background-color: linear-gradient(to right, #829cbc, #c4bdd6, rgba(196, 189, 214, 0.0));
                -fx-background-radius: 8;
                """;
    }

    public String mapRouteStyle() {
        return """
                -fx-background-color: linear-gradient(to right, transparent, rgba(175, 196, 216, 0.34), transparent);
                -fx-background-radius: 999;
                """;
    }

    public String featureChipStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 0% 0%, radius 120%, rgba(223, 230, 236, 0.07), transparent 62%),
                    rgba(24, 34, 56, 0.58);
                -fx-background-radius: 999;
                -fx-border-color: rgba(75, 93, 127, 0.42);
                -fx-border-radius: 999;
                -fx-border-width: 1;
                -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.16), 12, 0.18, 0, 4);
                """;
    }

    public String questCardStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 0% 0%, radius 120%, rgba(223, 230, 236, 0.08), transparent 62%),
                    linear-gradient(from 0% 0% to 100% 100%, rgba(24, 34, 56, 0.64), rgba(15, 19, 35, 0.70));
                -fx-background-radius: 18;
                -fx-border-color: rgba(75, 93, 127, 0.42);
                -fx-border-radius: 18;
                -fx-border-width: 1;
                -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.18), 16, 0.18, 0, 6);
                """;
    }

    public String questIconStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 35%, radius 86%, rgba(223, 230, 236, 0.22), transparent 62%),
                    rgba(16, 23, 42, 0.56);
                -fx-background-radius: 999;
                -fx-border-color: rgba(175, 196, 216, 0.38);
                -fx-border-radius: 999;
                -fx-border-width: 1;
                -fx-text-fill: #eef3f6;
                -fx-font-size: 17px;
                """;
    }

    public String featureChipTitleStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                """;
    }

    public String featureChipHintStyle() {
        return """
                -fx-text-fill: #aab8cf;
                -fx-font-size: 10px;
                """;
    }

    public String mapNodeStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 15% 0%, radius 80%, rgba(175, 196, 216, 0.10), transparent 58%),
                    radial-gradient(center 88% 100%, radius 84%, rgba(196, 189, 214, 0.08), transparent 62%),
                    linear-gradient(from 0% 0% to 100% 100%, rgba(24, 34, 56, 0.74), rgba(15, 19, 35, 0.74));
                -fx-background-radius: 18;
                -fx-border-color: rgba(75, 93, 127, 0.58);
                -fx-border-radius: 18;
                -fx-border-width: 1;
                -fx-effect:
                    dropshadow(gaussian, rgba(0, 0, 0, 0.22), 20, 0.22, 0, 8),
                    innershadow(gaussian, rgba(223, 230, 236, 0.04), 18, 0.15, 0, 0);
                """;
    }

    public String campaignBoardStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 14% 0%, radius 92%, rgba(223, 230, 236, 0.11), transparent 58%),
                    radial-gradient(center 100% 100%, radius 100%, rgba(196, 189, 214, 0.10), transparent 60%),
                    linear-gradient(from 0% 0% to 100% 100%, rgba(24, 34, 56, 0.82), rgba(15, 19, 35, 0.78));
                -fx-background-radius: 22;
                -fx-border-color: rgba(175, 196, 216, 0.42);
                -fx-border-radius: 22;
                -fx-border-width: 1;
                -fx-effect:
                    dropshadow(gaussian, rgba(0, 0, 0, 0.24), 24, 0.20, 0, 10),
                    innershadow(gaussian, rgba(223, 230, 236, 0.05), 20, 0.16, 0, 0);
                """;
    }

    public String boardIconStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 35%, radius 86%, rgba(223, 230, 236, 0.26), transparent 62%),
                    rgba(16, 23, 42, 0.58);
                -fx-background-radius: 999;
                -fx-border-color: rgba(175, 196, 216, 0.48);
                -fx-border-radius: 999;
                -fx-border-width: 1;
                -fx-text-fill: #eef3f6;
                -fx-font-size: 20px;
                -fx-font-weight: bold;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.18), 12, 0.22, 0, 0);
                """;
    }

    public String boardTitleStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                """;
    }

    public String boardHintStyle() {
        return """
                -fx-text-fill: #b6bed0;
                -fx-font-size: 12px;
                -fx-line-spacing: 3px;
                """;
    }

    public String boardRowStyle() {
        return """
                -fx-background-color: rgba(16, 23, 42, 0.38);
                -fx-background-radius: 12;
                -fx-border-color: rgba(75, 93, 127, 0.25);
                -fx-border-radius: 12;
                -fx-border-width: 1;
                """;
    }

    public String boardRowIconStyle() {
        return """
                -fx-background-color: rgba(223, 230, 236, 0.10);
                -fx-background-radius: 999;
                -fx-border-color: rgba(175, 196, 216, 0.30);
                -fx-border-radius: 999;
                -fx-border-width: 1;
                -fx-text-fill: #eef3f6;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                """;
    }

    public String compassFieldStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 50%, radius 56%, rgba(175, 196, 216, 0.11), transparent 64%),
                    radial-gradient(center 48% 52%, radius 92%, rgba(24, 34, 56, 0.42), transparent 74%);
                -fx-background-radius: 999;
                """;
    }

    public String starPointStyle() {
        return """
                -fx-fill: #d7e0ea;
                -fx-effect: dropshadow(gaussian, rgba(215, 224, 234, 0.46), 12, 0.24, 0, 0);
                """;
    }

    public String starRouteStyle() {
        return """
                -fx-stroke: rgba(183, 201, 221, 0.26);
                -fx-stroke-width: 1;
                """;
    }

    public String newsIconStyle() {
        return """
                -fx-fill: #b7c9dd;
                -fx-stroke: rgba(223, 230, 236, 0.62);
                -fx-stroke-width: 1;
                -fx-effect: dropshadow(gaussian, rgba(183, 201, 221, 0.32), 10, 0.24, 0, 0);
                """;
    }

    public String contactIconStyle() {
        return """
                -fx-fill: #c4bdd6;
                -fx-stroke: rgba(223, 230, 236, 0.52);
                -fx-stroke-width: 1;
                -fx-effect: dropshadow(gaussian, rgba(196, 189, 214, 0.30), 10, 0.24, 0, 0);
                """;
    }

    public String newsCardStyle() {
        return """
                -fx-background-color: transparent;
                -fx-background-radius: 14;
                """;
    }

    public String telegramPanelStyle() {
        return """
                -fx-background-color: transparent;
                -fx-background-radius: 14;
                """;
    }

    public String telegramFooterStyle() {
        return """
                -fx-background-color: rgba(10, 16, 31, 0.45);
                -fx-background-radius: 12;
                -fx-border-color: rgba(75, 93, 127, 0.48);
                -fx-border-radius: 12;
                -fx-border-width: 1;
                -fx-padding: 2 2 2 2;
                """;
    }

    public String newsCaptionStyle() {
        return """
                -fx-text-fill: #b7c9dd;
                -fx-font-size: 11px;
                -fx-font-weight: bold;
                -fx-effect: dropshadow(gaussian, rgba(183, 201, 221, 0.28), 7, 0.22, 0, 0);
                """;
    }

    public String newsTitleStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.16), 9, 0.22, 0, 0);
                """;
    }

    public String newsSummaryStyle() {
        return """
                -fx-text-fill: #e9edf3;
                -fx-font-size: 12px;
                -fx-line-spacing: 3px;
                """;
    }

    public String newsMetaStyle() {
        return """
                -fx-text-fill: #aab8cf;
                -fx-font-size: 11px;
                """;
    }

    public String releaseNewsSectionStyle() {
        return """
                -fx-background-color: rgba(10, 16, 31, 0.34);
                -fx-background-radius: 10;
                -fx-border-color: rgba(75, 93, 127, 0.30);
                -fx-border-radius: 10;
                -fx-border-width: 1;
                """;
    }

    public String releaseNewsSectionTitleStyle() {
        return """
                -fx-text-fill: #eef3f6;
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                """;
    }

    public String releaseNewsSectionMetaStyle() {
        return """
                -fx-text-fill: #8fa4bd;
                -fx-font-size: 10px;
                """;
    }

    public String eyebrowStyle() {
        return """
                -fx-text-fill: #b7c9dd;
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                -fx-effect: dropshadow(gaussian, rgba(183, 201, 221, 0.28), 8, 0.24, 0, 0);
                """;
    }

    public String titleStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 52px;
                -fx-font-weight: bold;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.24), 16, 0.24, 0, 2);
                """;
    }

    public String subtitleStyle() {
        return """
                -fx-text-fill: #d9e1ec;
                -fx-font-size: 15px;
                -fx-line-spacing: 4px;
                """;
    }

    public String sectionTitleStyle() {
        return """
                -fx-text-fill: #f0f2f7;
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                """;
    }

    public String actionHeaderStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 0% 0%, radius 100%, rgba(175, 196, 216, 0.08), transparent 62%),
                    rgba(18, 26, 48, 0.52);
                -fx-background-radius: 12;
                -fx-border-color: rgba(75, 93, 127, 0.42);
                -fx-border-radius: 12;
                -fx-border-width: 1;
                """;
    }

    public String actionSectionStyle() {
        return """
                -fx-background-color: rgba(11, 19, 35, 0.52);
                -fx-background-radius: 14;
                -fx-border-color: rgba(72, 85, 117, 0.34);
                -fx-border-radius: 14;
                -fx-border-width: 1;
                -fx-padding: 8;
                """;
    }

    public String actionSectionTitleStyle() {
        return """
                -fx-text-fill: #b7c9dd;
                -fx-font-size: 12px;
                -fx-font-weight: bold;
                -fx-letter-spacing: 0.5;
                """;
    }

    public String sectionHintStyle() {
        return """
                -fx-text-fill: #b6bed0;
                -fx-font-size: 12px;
                """;
    }

    public String compassCaptionStyle() {
        return """
                -fx-text-fill: #aebbd0;
                -fx-font-size: 11px;
                -fx-font-weight: bold;
                """;
    }

    public String logoSealStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 50%, radius 48%, rgba(223, 230, 236, 0.12), transparent 62%),
                    radial-gradient(center 50% 50%, radius 88%, rgba(175, 196, 216, 0.18), transparent 70%);
                -fx-background-radius: 999;
                """;
    }

    public String logoOuterRingStyle() {
        return """
                -fx-fill: rgba(24, 34, 56, 0.34);
                -fx-stroke: rgba(187, 201, 218, 0.82);
                -fx-stroke-width: 2.2;
                """;
    }

    public String logoMiddleRingStyle() {
        return """
                -fx-fill: transparent;
                -fx-stroke: rgba(175, 196, 216, 0.56);
                -fx-stroke-width: 1.4;
                """;
    }

    public String logoInnerRingStyle() {
        return """
                -fx-fill: rgba(16, 23, 42, 0.34);
                -fx-stroke: rgba(196, 189, 214, 0.58);
                -fx-stroke-width: 1.4;
                """;
    }

    public String logoDieStyle() {
        return """
                -fx-fill: linear-gradient(to bottom, #253149, #151d31);
                -fx-stroke: #dfe6ec;
                -fx-stroke-width: 2;
                -fx-effect: dropshadow(gaussian, rgba(175, 196, 216, 0.20), 14, 0.24, 0, 0);
                """;
    }

    public String logoFacetStyle() {
        return """
                -fx-stroke: rgba(223, 230, 236, 0.52);
                -fx-stroke-width: 1.1;
                """;
    }

    public String logoTextStyle() {
        return """
                -fx-text-fill: #f7faf5;
                -fx-font-size: 34px;
                -fx-font-weight: bold;
                -fx-effect: dropshadow(gaussian, rgba(223, 230, 236, 0.28), 10, 0.20, 0, 0);
                """;
    }

    public String iconBadgeStyle() {
        return """
                -fx-background-color:
                    radial-gradient(center 50% 35%, radius 90%, rgba(223, 230, 236, 0.18), transparent 64%),
                    rgba(16, 23, 42, 0.44);
                -fx-background-radius: 999;
                -fx-border-color: rgba(175, 196, 216, 0.28);
                -fx-border-radius: 999;
                -fx-border-width: 1;
                """;
    }

    public String iconGlyphStyle() {
        return iconGlyphStyle(18);
    }

    public String iconGlyphStyle(int fontSize) {
        return """
                -fx-text-fill: #eef3f6;
                -fx-font-size: %dpx;
                -fx-font-weight: bold;
                """.formatted(fontSize);
    }

    public String iconShapeStyle() {
        return iconShapeStyle(1.7);
    }

    public String iconShapeStyle(double strokeWidth) {
        return """
                -fx-fill: transparent;
                -fx-stroke: #eef3f6;
                -fx-stroke-width: %.1f;
                -fx-stroke-line-cap: round;
                -fx-effect: dropshadow(gaussian, rgba(238, 243, 246, 0.20), 8, 0.22, 0, 0);
                """.formatted(strokeWidth);
    }

    public String telegramPlaneStyle() {
        return filledIconShapeStyle();
    }

    public String filledIconShapeStyle() {
        return """
                -fx-fill: #eef3f6;
                -fx-stroke: rgba(238, 243, 246, 0.36);
                -fx-stroke-width: 1;
                -fx-effect: dropshadow(gaussian, rgba(183, 201, 221, 0.24), 8, 0.22, 0, 0);
                """;
    }

    public String footerStyle() {
        return """
                -fx-background-color: rgba(4, 6, 13, 0.88);
                -fx-border-color: rgba(75, 93, 127, 0.44);
                -fx-border-width: 1 0 0 0;
                """;
    }

    public String footerTextStyle() {
        return """
                -fx-text-fill: #aab8cf;
                -fx-font-size: 12px;
                """;
    }

    public void applyPrimaryAction(Button button) {
        applyButtonStyle(button, primaryActionStyle(false), primaryActionStyle(true));
    }

    public void applySecondaryAction(Button button) {
        applyButtonStyle(button, secondaryActionStyle(false), secondaryActionStyle(true));
    }

    public void applyUtilityAction(Button button) {
        applyButtonStyle(button, utilityActionStyle(false), utilityActionStyle(true));
    }

    public void applyDonateAction(Button button) {
        applyButtonStyle(button, donateActionStyle(false), donateActionStyle(true));
    }

    public void applyTelegramAction(Button button) {
        applyButtonStyle(button, telegramActionStyle(false, 12), telegramActionStyle(true, 12));
    }

    public void applyTelegramAction(Button button, int fontSize) {
        applyButtonStyle(button, telegramActionStyle(false, fontSize), telegramActionStyle(true, fontSize));
    }

    private void applyButtonStyle(Button button, String baseStyle, String hoverStyle) {
        button.setFocusTraversable(false);
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
        button.setOnMousePressed(e -> button.setTranslateY(1));
        button.setOnMouseReleased(e -> button.setTranslateY(0));
    }

    private String sharedActionStyle(int fontSize) {
        return """
                -fx-font-size: %dpx;
                -fx-font-weight: bold;
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                -fx-cursor: hand;
                -fx-padding: 0 14 0 14;
                """.formatted(fontSize);
    }

    private String primaryActionStyle(boolean hover) {
        String background = hover
                ? "linear-gradient(to bottom, #eef3f6, #c7d5df)"
                : "linear-gradient(to bottom, #dfe6ec, #b7c7d3)";
        String border = hover ? "#d8e4eb" : "#b3c4d3";
        String glow = hover ? "rgba(179, 196, 211, 0.48)" : "rgba(179, 196, 211, 0.28)";
        return sharedActionStyle(16) + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #0c1018;
                -fx-effect: dropshadow(gaussian, %s, 18, 0.26, 0, 3);
                """.formatted(background, border, glow);
    }

    private String secondaryActionStyle(boolean hover) {
        String background = hover ? "rgba(33, 45, 73, 0.88)" : "rgba(26, 36, 59, 0.74)";
        String border = hover ? "rgba(175, 196, 216, 0.52)" : "rgba(75, 93, 127, 0.38)";
        String glow = hover ? "rgba(175, 196, 216, 0.18)" : "rgba(0, 0, 0, 0.0)";
        return sharedActionStyle(14) + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: %s;
                -fx-effect: dropshadow(gaussian, %s, 12, 0.22, 0, 0);
                """.formatted(background, border, ASTRAL_TEXT, glow);
    }

    private String utilityActionStyle(boolean hover) {
        String background = hover ? "rgba(39, 47, 79, 0.86)" : "rgba(24, 31, 54, 0.72)";
        String border = hover ? "rgba(196, 189, 214, 0.58)" : "rgba(196, 189, 214, 0.28)";
        return sharedActionStyle(12) + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #e8e8f3;
                """.formatted(background, border);
    }

    private String donateActionStyle(boolean hover) {
        String background = hover ? "#4b2b42" : DONATE_WINE;
        String border = hover ? "rgba(133, 96, 120, 0.78)" : "rgba(133, 96, 120, 0.48)";
        return sharedActionStyle(12) + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #f1e7ee;
                -fx-effect: dropshadow(gaussian, rgba(133, 96, 120, 0.20), 12, 0.24, 0, 0);
                """.formatted(background, border);
    }

    private String telegramActionStyle(boolean hover, int fontSize) {
        String background = hover ? "rgba(42, 53, 86, 0.88)" : "rgba(25, 36, 62, 0.76)";
        String border = hover ? "rgba(175, 196, 216, 0.56)" : "rgba(75, 93, 127, 0.42)";
        String glow = hover ? "rgba(175, 196, 216, 0.18)" : "rgba(175, 196, 216, 0.07)";
        return sharedActionStyle(fontSize) + """
                -fx-background-color: %s;
                -fx-border-color: %s;
                -fx-text-fill: #e9edf3;
                -fx-effect: dropshadow(gaussian, %s, 12, 0.22, 0, 0);
                """.formatted(background, border, glow);
    }
}
