package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.CharacterUseCases;
import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.service.CharacterTransferService;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Default JavaFX implementation of StartScreenFlowFactory.
 */
public class DefaultStartScreenFlowFactory implements StartScreenFlowFactory {

    private final Stage stage;
    private final ScreenNavigator screenNavigator;
    private final CharacterUseCases characterUseCases;
    private final CharacterTransferService characterTransferService;

    public DefaultStartScreenFlowFactory(
            Stage stage,
            ScreenNavigator screenNavigator,
            CharacterUseCases characterUseCases,
            CharacterTransferService characterTransferService
    ) {
        this.stage = Objects.requireNonNull(stage, "stage must not be null");
        this.screenNavigator = Objects.requireNonNull(screenNavigator, "screenNavigator must not be null");
        this.characterUseCases = Objects.requireNonNull(characterUseCases, "characterUseCases must not be null");
        this.characterTransferService = Objects.requireNonNull(characterTransferService, "characterTransferService must not be null");
    }

    @Override
    public Parent createCharacterCreate(Runnable backToStartAction) {
        CharacterCreateScreen createScreen = new CharacterCreateScreen(
                stage,
                screenNavigator,
                characterUseCases.saveCharacterUseCase(),
                backToStartAction
        );
        return createScreen.getView();
    }

    @Override
    public Parent createCharacterEdit(Character character, Runnable backToStartAction) {
        CharacterEditScreen editScreen = new CharacterEditScreen(
                stage,
                character,
                screenNavigator,
                characterUseCases.saveCharacterUseCase(),
                backToStartAction
        );
        return editScreen.getView();
    }

    @Override
    public Parent createCharacterOverview(Character character, Runnable backToStartAction) {
        return new CharacterOverviewScreen(
                stage,
                character,
                screenNavigator,
                characterUseCases.saveCharacterUseCase(),
                backToStartAction
        );
    }

    @Override
    public Parent createCharacterSelection(
            boolean isEdit,
            Consumer<Character> onCharacterSelected,
            Runnable backToStartAction
    ) {
        CharacterSelectionController selectionController = new CharacterSelectionController(
                characterUseCases.listCharacterNamesUseCase(),
                characterUseCases.loadCharacterUseCase(),
                characterUseCases.deleteCharacterUseCase(),
                backToStartAction
        );
        return new CharacterSelectionScreen(onCharacterSelected, isEdit, selectionController);
    }

    @Override
    public Parent createCharacterTransfer(Runnable backToStartAction) {
        CharacterImportExportController controller = new CharacterImportExportController(
                characterTransferService,
                characterUseCases.listCharacterNamesUseCase(),
                characterUseCases.loadCharacterUseCase(),
                backToStartAction
        );
        return new CharacterImportExportScreen(stage, controller);
    }

    @Override
    public Parent createAssetManager(Runnable backToStartAction) {
        return new AssetManagerScreen(stage, backToStartAction);
    }
}

