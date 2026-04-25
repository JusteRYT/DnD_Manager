package com.example.dnd_manager.screen;

import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.application.usecase.character.ListCharacterNamesUseCase;
import com.example.dnd_manager.lang.I18n;
import com.example.dnd_manager.service.CharacterTransferServiceImpl;
import com.example.dnd_manager.store.StorageService;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

/**
 * Handles StartScreen navigation and flow decisions.
 */
public class StartScreenController {

    private static final Logger log = LoggerFactory.getLogger(StartScreenController.class);

    private final Stage stage;
    private final StorageService storageService;
    private final ScreenNavigator screenNavigator;
    private final ListCharacterNamesUseCase listCharacterNamesUseCase;
    private final BiConsumer<String, String> errorPresenter;

    public StartScreenController(
            Stage stage,
            StorageService storageService,
            ScreenNavigator screenNavigator,
            ListCharacterNamesUseCase listCharacterNamesUseCase,
            BiConsumer<String, String> errorPresenter
    ) {
        this.stage = stage;
        this.storageService = storageService;
        this.screenNavigator = screenNavigator;
        this.listCharacterNamesUseCase = listCharacterNamesUseCase;
        this.errorPresenter = errorPresenter;
    }

    public void openCharacterCreate() {
        log.info("Navigation: Opening Character Creation screen");
        CharacterCreateScreen createScreen = new CharacterCreateScreen(stage, storageService);
        screenNavigator.open(createScreen.getView());
    }

    public void openCharacterEdit() {
        List<String> names = listCharacterNamesUseCase.execute();
        if (names.isEmpty()) {
            errorPresenter.accept(I18n.t("error.no_characters_title"), I18n.t("error.no_characters_msg"));
            return;
        }

        CharacterSelectionScreen selectionScreen = new CharacterSelectionScreen(
                stage,
                storageService,
                character -> {
                    CharacterEditScreen editScreen = new CharacterEditScreen(stage, character, storageService);
                    screenNavigator.open(editScreen.getView());
                },
                true
        );

        screenNavigator.open(selectionScreen);
    }

    public void openCharacterLoad() {
        log.info("Navigation: Opening Character Selection (Load/View mode)");
        List<String> names = listCharacterNamesUseCase.execute();
        if (names.isEmpty()) {
            log.warn("Navigation failed: No characters available for loading");
            errorPresenter.accept(I18n.t("error.no_characters_title"), I18n.t("error.no_characters_msg"));
            return;
        }

        CharacterSelectionScreen selectionScreen = new CharacterSelectionScreen(
                stage,
                storageService,
                character -> {
                    log.info("Character selected for view: {}", character.getName());
                    CharacterOverviewScreen overviewScreen = new CharacterOverviewScreen(stage, character, storageService);
                    screenNavigator.open(overviewScreen);
                },
                false
        );

        screenNavigator.open(selectionScreen);
    }

    public void openCharacterTransfer() {
        log.info("Navigation: Opening Import/Export screen");
        CharacterImportExportScreen screen = new CharacterImportExportScreen(
                stage,
                storageService,
                new CharacterTransferServiceImpl()
        );
        screenNavigator.open(screen);
    }

    public void openAssetManager() {
        AssetManagerScreen assetScreen = new AssetManagerScreen(stage, storageService);
        screenNavigator.open(assetScreen);
    }

    public void changeLanguageAndReload() {
        Locale newLocale = I18n.isEnglish() ? Locale.forLanguageTag("ru") : Locale.ENGLISH;
        log.info("UI: Changing language to {}", newLocale);
        I18n.setLocale(newLocale);
        screenNavigator.open(new StartScreen(stage, storageService).getView());
    }
}

