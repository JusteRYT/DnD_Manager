package com.example.dnd_manager.screen.start;

import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.updater.model.GitHubRelease;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class StartScreenNewsPanel extends VBox {

    private final StartScreenStyleProvider styles;
    private final Label titleLabel = new Label();
    private final VBox summaryItems = new VBox(6);
    private final Label metaLabel = new Label();
    private final StartScreenReleaseNewsFormatter formatter = new StartScreenReleaseNewsFormatter();

    public StartScreenNewsPanel(StartScreenStyleProvider styles, Supplier<List<GitHubRelease>> releaseSupplier) {
        this.styles = styles;
        setSpacing(8);
        setPadding(new Insets(14));
        setMinWidth(220);
        setPrefWidth(255);
        setMaxWidth(320);
        setStyle(styles.newsCardStyle());

        Circle icon = new Circle(5);
        icon.setStyle(styles.newsIconStyle());

        Label caption = new Label(I18n.t("news.title"));
        caption.setStyle(styles.newsCaptionStyle());

        HBox header = new HBox(8, icon, caption);
        header.setAlignment(Pos.CENTER_LEFT);

        titleLabel.setWrapText(true);
        titleLabel.setStyle(styles.newsTitleStyle());

        summaryItems.setPadding(new Insets(2, 0, 2, 0));

        metaLabel.setWrapText(true);
        metaLabel.setStyle(styles.newsMetaStyle());

        getChildren().addAll(header, titleLabel, summaryItems, metaLabel);
        show(formatter.fallback());
        loadRelease(releaseSupplier);
    }

    private void loadRelease(Supplier<List<GitHubRelease>> releaseSupplier) {
        CompletableFuture
                .supplyAsync(releaseSupplier)
                .thenApply(formatter::fromReleases)
                .exceptionally(ex -> formatter.fallback())
                .thenAccept(news -> Platform.runLater(() -> show(news)));
    }

    private void show(StartScreenReleaseNews news) {
        titleLabel.setText(news.title());
        summaryItems.getChildren().clear();
        if (news.sections() != null && !news.sections().isEmpty()) {
            showSections(news.sections());
            metaLabel.setManaged(false);
            metaLabel.setVisible(false);
            return;
        } else {
            showFlatSummary(news.summary());
        }
        metaLabel.setText(news.meta());
        boolean showMeta = shouldShowMeta(news);
        metaLabel.setManaged(showMeta);
        metaLabel.setVisible(showMeta);
    }

    private void showSections(List<StartScreenReleaseNewsSection> sections) {
        for (StartScreenReleaseNewsSection section : sections) {
            VBox releaseBlock = new VBox(5);
            releaseBlock.setPadding(new Insets(8, 9, 8, 9));
            releaseBlock.setStyle(styles.releaseNewsSectionStyle());

            Label releaseTitle = new Label(section.title());
            releaseTitle.setWrapText(true);
            releaseTitle.setStyle(styles.releaseNewsSectionTitleStyle());
            releaseBlock.getChildren().add(releaseTitle);

            for (String highlight : section.highlights()) {
                if (highlight == null || highlight.isBlank()) {
                    continue;
                }
                Label item = new Label("• " + highlight.trim());
                item.setWrapText(true);
                item.setStyle(styles.newsSummaryStyle());
                releaseBlock.getChildren().add(item);
            }

            Label releaseMeta = new Label(section.meta());
            releaseMeta.setWrapText(true);
            releaseMeta.setStyle(styles.releaseNewsSectionMetaStyle());
            releaseBlock.getChildren().add(releaseMeta);

            summaryItems.getChildren().add(releaseBlock);
        }
    }

    private void showFlatSummary(String summaryText) {
        if (summaryText == null || summaryText.isBlank()) {
            Label item = new Label(I18n.t("news.emptyReleaseBody"));
            item.setWrapText(true);
            item.setStyle(styles.newsSummaryStyle());
            summaryItems.getChildren().add(item);
        } else {
            String[] lines = summaryText.split("\\R");
            for (String rawLine : lines) {
                String itemText = rawLine.trim();
                if (itemText.isBlank()) {
                    continue;
                }
                Label item = new Label("• " + itemText);
                item.setWrapText(true);
                item.setStyle(styles.newsSummaryStyle());
                summaryItems.getChildren().add(item);
            }
        }
    }

    private boolean shouldShowMeta(StartScreenReleaseNews news) {
        String meta = news.meta();
        if (meta == null || meta.isBlank()) {
            return false;
        }

        String normalizedMeta = meta.trim().toLowerCase();
        String title = news.title() == null ? "" : news.title().toLowerCase();
        String summary = news.summary() == null ? "" : news.summary().toLowerCase();
        return !title.contains(normalizedMeta) && !summary.contains(normalizedMeta);
    }
}
