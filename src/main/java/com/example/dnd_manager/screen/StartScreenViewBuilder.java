package com.example.dnd_manager.screen;

import com.example.dnd_manager.info.version.AppInfo;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.theme.AppTheme;
import com.example.dnd_manager.theme.ButtonSizeConfigurer;
import com.example.dnd_manager.theme.factory.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

/**
 * Builds StartScreen JavaFX view.
 */
public class StartScreenViewBuilder {

    private static final double SCALE = 2;
    private static final int MAIN_BUTTON_WIDTH = 400;
    private static final int MAIN_BUTTON_HEIGHT = 100;
    private static final int MAIN_BUTTON_FONT_SIZE = 20;

    public Parent build(StartScreenViewActions actions) {
        Objects.requireNonNull(actions, "actions must not be null");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: %s;".formatted(AppTheme.BACKGROUND_PRIMARY));

        VBox content = new VBox(18 * SCALE);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40 * SCALE));
        content.setFillWidth(false);

        Label title = buildTitle();
        ImageView diceImage = buildDiceImage();

        Button createButton = AppButtonFactory.primaryButton(
                I18n.t("button.create"), MAIN_BUTTON_WIDTH, MAIN_BUTTON_HEIGHT, MAIN_BUTTON_FONT_SIZE
        );
        Button editButton = AppButtonFactory.primaryButton(
                I18n.t("button.edit"), MAIN_BUTTON_WIDTH, MAIN_BUTTON_HEIGHT, MAIN_BUTTON_FONT_SIZE
        );
        Button loadButton = AppButtonFactory.primaryButton(
                I18n.t("button.load"), MAIN_BUTTON_WIDTH, MAIN_BUTTON_HEIGHT, MAIN_BUTTON_FONT_SIZE
        );
        Button assetManagerBtn = AppButtonFactory.primaryButton(
                I18n.t("button.assets"), MAIN_BUTTON_WIDTH, MAIN_BUTTON_HEIGHT, MAIN_BUTTON_FONT_SIZE
        );
        Button transferButton = AppButtonFactory.primaryButton(
                I18n.t("button.importExport"), MAIN_BUTTON_WIDTH, MAIN_BUTTON_HEIGHT, MAIN_BUTTON_FONT_SIZE
        );

        Button languageBtn = AppButtonFactory.primaryButton(
                I18n.t("button.language"), 120, 40, 14
        );
        Button updateBtn = AppButtonFactory.primaryButton(
                I18n.t("button.checkUpdate"), 120, 40, 14
        );

        ButtonSizeConfigurer.applyFixedSize(createButton, 400, 50);
        ButtonSizeConfigurer.applyFixedSize(editButton, 400, 50);
        ButtonSizeConfigurer.applyFixedSize(loadButton, 400, 50);
        ButtonSizeConfigurer.applyFixedSize(assetManagerBtn, 400, 50);
        ButtonSizeConfigurer.applyFixedSize(transferButton, 400, 50);
        ButtonSizeConfigurer.applyFixedSize(languageBtn, 150, 40);
        ButtonSizeConfigurer.applyFixedSize(updateBtn, 150, 40);

        languageBtn.setOnAction(e -> actions.onLanguageChange().run());
        updateBtn.setOnAction(e -> actions.onUpdateCheck().accept(updateBtn));
        languageBtn.setFocusTraversable(false);
        updateBtn.setFocusTraversable(false);

        createButton.setOnAction(e -> actions.onCreate().run());
        editButton.setOnAction(e -> actions.onEdit().run());
        loadButton.setOnAction(e -> actions.onLoad().run());
        transferButton.setOnAction(e -> actions.onTransfer().run());
        assetManagerBtn.setOnAction(e -> actions.onAssets().run());

        HBox bottomButtons = new HBox(15);
        bottomButtons.setAlignment(Pos.CENTER);
        bottomButtons.getChildren().addAll(languageBtn, updateBtn);

        content.getChildren().addAll(
                title,
                diceImage,
                createButton,
                editButton,
                loadButton,
                assetManagerBtn,
                transferButton,
                bottomButtons
        );

        root.setCenter(content);
        root.setBottom(buildVersionFooter());
        return root;
    }

    private Label buildTitle() {
        Label title = new Label(I18n.t("title.start"));
        title.setStyle("""
                    -fx-text-fill: %s;
                    -fx-font-size: %dpx;
                    -fx-font-weight: bold;
                """.formatted(
                AppTheme.TEXT_ACCENT,
                (int) (36 * SCALE)
        ));
        return title;
    }

    private ImageView buildDiceImage() {
        ImageView imageView = new ImageView(
                new Image(
                        Objects.requireNonNull(
                                getClass().getResource("/com/example/dnd_manager/icon/icon_dice.png")
                        ).toExternalForm()
                )
        );
        imageView.setFitWidth(120 * SCALE);
        imageView.setFitHeight(120 * SCALE);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private HBox buildVersionFooter() {
        Label versionLabel = new Label("v" + AppInfo.getVersion());
        versionLabel.setStyle("""
                -fx-text-fill: #777777;
                -fx-font-size: 16px;
                """);

        HBox footer = new HBox(versionLabel);
        footer.setAlignment(Pos.CENTER_RIGHT);
        footer.setPadding(new Insets(5, 10, 5, 10));
        return footer;
    }
}

