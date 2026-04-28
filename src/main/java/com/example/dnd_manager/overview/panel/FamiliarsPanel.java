package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class FamiliarsPanel extends VBox {
    private final Character character;
    private final VBox listContainer;
    private final FamiliarsPanelController controller;
    private final FamiliarCardViewModelMapper cardMapper;
    private final FamiliarCardBuilder cardBuilder;
    private final FamiliarsPanelStyleProvider styleProvider;

    public FamiliarsPanel(Character character, Stage parentStage, SaveCharacterUseCase saveCharacterUseCase) {
        this(
                character,
                new FamiliarsPanelController(parentStage, character, saveCharacterUseCase),
                new FamiliarCardViewModelMapper(),
                new FamiliarCardBuilder(),
                new FamiliarsPanelStyleProvider()
        );
    }

    FamiliarsPanel(
            Character character,
            FamiliarsPanelController controller,
            FamiliarCardViewModelMapper cardMapper,
            FamiliarCardBuilder cardBuilder,
            FamiliarsPanelStyleProvider styleProvider
    ) {
        this.character = character;
        this.controller = Objects.requireNonNull(controller, "controller must not be null");
        this.cardMapper = Objects.requireNonNull(cardMapper, "cardMapper must not be null");
        this.cardBuilder = Objects.requireNonNull(cardBuilder, "cardBuilder must not be null");
        this.styleProvider = Objects.requireNonNull(styleProvider, "styleProvider must not be null");

        // Заголовок
        Label title = new Label(I18n.t("label.familiars"));
        title.setStyle(styleProvider.titleStyle());
        title.setPadding(new Insets(0, 0, 10, 0));

        // Контейнер для списка
        this.listContainer = new VBox(8);

        refresh();

        getChildren().addAll(title, listContainer);
        setStyle(styleProvider.idleStyle());
        setOnMouseEntered(e -> setStyle(styleProvider.hoverStyle()));
        setOnMouseExited(e -> setStyle(styleProvider.idleStyle()));
    }

    private HBox createFamiliarCard(Character familiar) {
        FamiliarCardViewModel vm = cardMapper.map(familiar);
        return cardBuilder.build(character.getName(), familiar, vm, () -> controller.openFamiliar(familiar, this::refresh));
    }

    public void refresh() {
        listContainer.getChildren().clear();
        if (character.getFamiliars().isEmpty()) {
            Label emptyLabel = new Label(I18n.t("label.noFamiliars"));
            emptyLabel.setStyle(styleProvider.emptyLabelStyle());
            listContainer.getChildren().add(emptyLabel);
        } else {
            for (Character familiar : character.getFamiliars()) {
                listContainer.getChildren().add(createFamiliarCard(familiar));
            }
        }
    }
}












