package com.example.dnd_manager.overview.ui.topbar;

import com.example.dnd_manager.overview.ui.launchers.StageResolver;
import com.example.dnd_manager.theme.button.AppButtonFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.function.Consumer;

public class TopBarActionsRowBuilder {

    private final TopBarActionsStyleProvider styleProvider;

    public TopBarActionsRowBuilder() {
        this(new TopBarActionsStyleProvider());
    }

    TopBarActionsRowBuilder(TopBarActionsStyleProvider styleProvider) {
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");
    }

    public TopBarActionButtons build(
            TopBarController controller,
            StageResolver stageResolver,
            Consumer<Stage> openEditStats,
            Consumer<Stage> openLevelUp
    ) {
        Button exportBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/import-export.png");
        exportBtn.setOnAction(e -> controller.exportDescription(stageResolver.resolve(exportBtn)));

        Button showDescBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/icon_description.png");
        showDescBtn.setOnAction(e -> controller.showDescription(stageResolver.resolve(showDescBtn)));

        Button editBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/edit_icon.png");
        editBtn.setOnAction(e -> openEditStats.accept(stageResolver.resolve(editBtn)));

        Button backBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/icon_back.png");
        backBtn.setOnAction(e -> controller.backToStart());

        Button increaseLevelBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/level_up_icon.png");
        increaseLevelBtn.setOnAction(e -> openLevelUp.accept(stageResolver.resolve(increaseLevelBtn)));

        Button notesBtn = AppButtonFactory.hudIconButton(50, "/com/example/dnd_manager/icon/icon_notes.png");
        notesBtn.setOnAction(e -> controller.showNotes(stageResolver.resolve(notesBtn)));

        HBox buttonsRow = new HBox(15,
                exportBtn,
                showDescBtn,
                notesBtn,
                editBtn,
                increaseLevelBtn,
                backBtn
        );
        buttonsRow.setAlignment(Pos.CENTER);
        buttonsRow.setPadding(new Insets(15, 20, 15, 20));
        buttonsRow.setMaxHeight(100);
        buttonsRow.setStyle(styleProvider.actionsRowStyle());

        return new TopBarActionButtons(
                buttonsRow,
                exportBtn,
                showDescBtn,
                notesBtn,
                editBtn,
                increaseLevelBtn,
                backBtn
        );
    }
}












