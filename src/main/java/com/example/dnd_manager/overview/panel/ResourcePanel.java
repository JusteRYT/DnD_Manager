package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.ui.currency.CurrencyBox;
import com.example.dnd_manager.overview.ui.resources.ManaBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.Getter;

public class ResourcePanel extends HBox {

    @Getter
    private final ManaBar manaBar;

    public ResourcePanel(Character character, SaveCharacterUseCase saveCharacterUseCase) {
        this(
                character,
                saveCharacterUseCase,
                new CurrencyBox(character, saveCharacterUseCase),
                new ResourcePanelRightStackBuilder(),
                new ResourcePanelStyleProvider()
        );
    }

    ResourcePanel(
            Character character,
            SaveCharacterUseCase saveCharacterUseCase,
            CurrencyBox currencyBox,
            ResourcePanelRightStackBuilder rightStackBuilder,
            ResourcePanelStyleProvider styleProvider
    ) {
        setSpacing(15);
        setPadding(new Insets(5));
        setAlignment(Pos.TOP_LEFT);
        setFillHeight(false);
        setStyle(styleProvider.containerStyle());

        HBox.setHgrow(currencyBox, Priority.ALWAYS);
        currencyBox.setMaxWidth(Double.MAX_VALUE);
        currencyBox.setMinHeight(180);

        ResourcePanelRightStack rightStack = rightStackBuilder.build(character, saveCharacterUseCase);
        this.manaBar = rightStack.manaBar();

        getChildren().addAll(currencyBox, rightStack.container());
    }
}












