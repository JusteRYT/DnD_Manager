package com.example.dnd_manager.overview.dialogs.familiar;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.dialogs.common.BaseDialog;
import com.example.dnd_manager.overview.ui.resources.HpBar;
import com.example.dnd_manager.overview.ui.resources.ManaBar;
import com.example.dnd_manager.theme.scroll.AppScrollPaneFactory;
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
    private final FamiliarResourceSnapshotFactory resourceSnapshotFactory;
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
                new FamiliarSectionBuilder(),
                new FamiliarResourceSnapshotFactory()
        );
    }

    FamiliarInfoDialog(
            Stage ownerStage,
            Character familiar,
            Character owner,
            SaveCharacterUseCase saveCharacterUseCase,
            FamiliarHeaderBuilder familiarHeaderBuilder,
            FamiliarSectionBuilder familiarSectionBuilder,
            FamiliarResourceSnapshotFactory resourceSnapshotFactory
    ) {
        super(ownerStage, familiar.getName(), 550, 700);
        this.familiar = familiar;
        this.owner = owner;
        this.saveCharacterUseCase = saveCharacterUseCase;
        this.familiarHeaderBuilder = familiarHeaderBuilder;
        this.familiarSectionBuilder = familiarSectionBuilder;
        this.resourceSnapshotFactory = resourceSnapshotFactory;
    }

    @Override
    protected void setupContent() {
        contentArea.setSpacing(15);
        contentArea.setPadding(new Insets(15, 25, 25, 25));

        Label hpValLabel = createResourceValueLabel("#d98a9a");
        Label mpValLabel = createResourceValueLabel("#8fd0c8");
        Label acValLabel = createResourceValueLabel("#7fb9d4");
        Label lvlValLabel = createResourceValueLabel("#c4bdd6");

        FamiliarResourcePresenter resourcePresenter = new FamiliarResourcePresenter(
                resourceSnapshotFactory,
                new JavaFxFamiliarResourceDisplay(hpValLabel, mpValLabel, acValLabel, lvlValLabel)
        );
        resourcePresenter.refresh(familiar);

        HpBar hpBar = new HpBar(familiar, owner, saveCharacterUseCase);
        ManaBar manaBar = new ManaBar(familiar, owner, saveCharacterUseCase);

        hpBar.setOnUpdate(resourcePresenter.updateHandler(familiar, onAnyUpdate));
        manaBar.setOnUpdate(resourcePresenter.updateHandler(familiar, onAnyUpdate));

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

    private Label createResourceValueLabel(String color) {
        Label label = new Label();
        label.setStyle(dialogStyles.resourceValueStyle(color));
        return label;
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












