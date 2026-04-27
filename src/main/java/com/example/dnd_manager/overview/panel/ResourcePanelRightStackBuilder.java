package com.example.dnd_manager.overview.panel;

import com.example.dnd_manager.application.usecase.character.SaveCharacterUseCase;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.overview.ui.InspirationBox;
import com.example.dnd_manager.overview.ui.ManaBar;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class ResourcePanelRightStackBuilder {

    public ResourcePanelRightStack build(Character character, SaveCharacterUseCase saveCharacterUseCase) {
        VBox rightStack = new VBox(10);
        rightStack.setAlignment(Pos.TOP_CENTER);

        InspirationBox inspirationBox = new InspirationBox(character, saveCharacterUseCase);
        ManaBar manaBar = new ManaBar(character, character, saveCharacterUseCase);

        rightStack.setMinWidth(300);
        rightStack.setPrefWidth(450);

        inspirationBox.setMaxWidth(Double.MAX_VALUE);
        manaBar.setMaxWidth(Double.MAX_VALUE);

        rightStack.getChildren().addAll(inspirationBox, manaBar);
        return new ResourcePanelRightStack(rightStack, manaBar);
    }
}

