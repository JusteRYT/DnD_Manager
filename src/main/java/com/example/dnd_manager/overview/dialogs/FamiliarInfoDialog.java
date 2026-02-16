package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class FamiliarInfoDialog extends BaseDialog {

    private final Character familiar;
    private final Character owner;

    public FamiliarInfoDialog(Stage ownerStage, Character familiar, Character owner) {
        super(ownerStage, familiar.getName(), 550, 750);
        this.familiar = familiar;
        this.owner = owner;
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(15);
        contentArea.setPadding(new Insets(15, 25, 25, 25));

        VBox mainScrollContent = new VBox(20);
        mainScrollContent.getChildren().addAll(
                FamiliarSectionBuilder.buildResources(familiar),
                FamiliarSectionBuilder.buildStats(familiar),
                FamiliarSectionBuilder.buildIconLists(familiar, owner)
        );
        FamiliarSectionBuilder.addLore(mainScrollContent, familiar);
        Separator separator = new Separator();
        separator.setOpacity(0.2);

        contentArea.getChildren().addAll(
                buildHeader(),
                separator,
                wrapInScrollPane(mainScrollContent)
        );
    }

    private HBox buildHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        ImageView avatar = new ImageView();
        avatar.setFitWidth(80); avatar.setFitHeight(80);
        try {
            avatar.setImage(new Image(CharacterAssetResolver.resolve(owner.getName(), familiar.getAvatarImage())));
        } catch (Exception e) {
            avatar.setImage(new Image(getClass().getResource("/com/example/dnd_manager/icon/no_image.png").toExternalForm()));
        }
        avatar.setClip(new Circle(40, 40, 40));

        VBox info = new VBox(2);
        Label name = new Label(familiar.getName());
        name.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #9c27b0;");
        String metaText = String.format("%s • %s • %s %s", familiar.getRace(), familiar.getCharacterClass(), I18n.t("label.familiarsLvl"), familiar.getLevel());
        Label meta = new Label(metaText);
        meta.setStyle("-fx-text-fill: #888;");

        info.getChildren().addAll(name, meta);
        header.getChildren().addAll(avatar, info);
        return header;
    }

    private ScrollPane wrapInScrollPane(VBox content) {
        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(sp, Priority.ALWAYS);
        return sp;
    }
}