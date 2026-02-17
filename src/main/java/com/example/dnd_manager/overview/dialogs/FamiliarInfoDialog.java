package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.overview.ui.HpBar;
import com.example.dnd_manager.overview.ui.ManaBar;
import com.example.dnd_manager.repository.CharacterAssetResolver;
import com.example.dnd_manager.store.StorageService;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
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
import lombok.Setter;

public class FamiliarInfoDialog extends BaseDialog {

    private final Character familiar;
    private final Character owner;
    private final StorageService storageService;
    @Setter
    private Runnable onAnyUpdate;

    public FamiliarInfoDialog(Stage ownerStage, Character familiar, Character owner, StorageService storageService) {
        super(ownerStage, familiar.getName(), 550, 700);
        this.familiar = familiar;
        this.owner = owner;
        this.storageService = storageService;
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(15);
        contentArea.setPadding(new Insets(15, 25, 25, 25));

        Label hpValLabel = new Label(familiar.getCurrentHp() + "/" + familiar.getMaxHp());
        hpValLabel.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 15px; -fx-font-weight: bold;");

        Label mpValLabel = new Label(familiar.getCurrentMana() + "/" + familiar.getMaxMana());
        mpValLabel.setStyle("-fx-text-fill: #4dabf7; -fx-font-size: 15px; -fx-font-weight: bold;");

        Label acValLabel = new Label(String.valueOf(familiar.getArmor()));
        acValLabel.setStyle("-fx-text-fill: #74c0fc; -fx-font-size: 15px; -fx-font-weight: bold;");

        Label lvlValLabel = new Label(String.valueOf(familiar.getLevel()));
        lvlValLabel.setStyle("-fx-text-fill: #ff922b; -fx-font-size: 15px; -fx-font-weight: bold;");

        HpBar hpBar = new HpBar(familiar, owner, storageService);
        ManaBar manaBar = new ManaBar(familiar, owner, storageService);

        hpBar.setOnUpdate(() -> {
            hpValLabel.setText(familiar.getCurrentHp() + "/" + familiar.getMaxHp());
            if (onAnyUpdate != null) onAnyUpdate.run();
        });

        manaBar.setOnUpdate(() -> {
            mpValLabel.setText(familiar.getCurrentMana() + "/" + familiar.getMaxMana());
            if (onAnyUpdate != null) onAnyUpdate.run();
        });

        VBox mainScrollContent = new VBox(20);

        mainScrollContent.getChildren().addAll(
                hpBar,
                manaBar,
                FamiliarSectionBuilder.buildResources(hpValLabel, acValLabel, mpValLabel, lvlValLabel),
                FamiliarSectionBuilder.buildStats(familiar),
                FamiliarSectionBuilder.buildIconLists(familiar, owner)
        );
        FamiliarSectionBuilder.addLore(mainScrollContent, familiar);
        Separator separator = new Separator();
        separator.setOpacity(0.2);

        contentArea.getChildren().addAll(
                buildHeader(),
                separator,
                wrapInAppScrollPane(mainScrollContent)
        );
    }

    private HBox buildHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        ImageView avatar = new ImageView();
        avatar.setFitWidth(80);
        avatar.setFitHeight(80);
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

    private ScrollPane wrapInAppScrollPane(VBox content) {
        content.setPadding(new Insets(10));
        ScrollPane sp = AppScrollPaneFactory.defaultPane(content);
        sp.setFitToWidth(true);
        sp.setFitToHeight(false);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-width: 0;");

        VBox.setVgrow(sp, Priority.ALWAYS);
        return sp;
    }
}