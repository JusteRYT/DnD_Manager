package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.ui.HpBar;
import com.example.dnd_manager.overview.ui.ManaBar;
import com.example.dnd_manager.theme.factory.AppScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Setter;

public class FamiliarInfoDialog extends BaseDialog {

    private final Character familiar;
    private final Character owner;
    private final SaveCharacterUseCase saveCharacterUseCase;
    private final FamiliarHeaderBuilder familiarHeaderBuilder;
    private final FamiliarSectionBuilder familiarSectionBuilder;
    @Setter
    private Runnable onAnyUpdate;

    public FamiliarInfoDialog(
            Stage ownerStage,
            Character familiar,
            Character owner,
            SaveCharacterUseCase saveCharacterUseCase
    ) {
        this(
                ownerStage,
                familiar,
                owner,
                saveCharacterUseCase,
                new FamiliarHeaderBuilder(),
                new FamiliarSectionBuilder()
        );
    }

    FamiliarInfoDialog(
            Stage ownerStage,
            Character familiar,
            Character owner,
            SaveCharacterUseCase saveCharacterUseCase,
            FamiliarHeaderBuilder familiarHeaderBuilder,
            FamiliarSectionBuilder familiarSectionBuilder
    ) {
        super(ownerStage, familiar.getName(), 550, 700);
        this.familiar = familiar;
        this.owner = owner;
        this.saveCharacterUseCase = saveCharacterUseCase;
        this.familiarHeaderBuilder = familiarHeaderBuilder;
        this.familiarSectionBuilder = familiarSectionBuilder;
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

        HpBar hpBar = new HpBar(familiar, owner, saveCharacterUseCase);
        ManaBar manaBar = new ManaBar(familiar, owner, saveCharacterUseCase);

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
                familiarSectionBuilder.buildResources(hpValLabel, acValLabel, mpValLabel, lvlValLabel),
                familiarSectionBuilder.buildStats(familiar),
                familiarSectionBuilder.buildIconLists(familiar, owner)
        );
        familiarSectionBuilder.addLore(mainScrollContent, familiar);
        Separator separator = new Separator();
        separator.setOpacity(0.2);

        contentArea.getChildren().addAll(
                familiarHeaderBuilder.build(familiar, owner),
                separator,
                wrapInAppScrollPane(mainScrollContent)
        );
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
