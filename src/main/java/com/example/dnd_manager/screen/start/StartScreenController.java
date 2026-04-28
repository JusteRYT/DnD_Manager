package com.example.dnd_manager.screen.start;

import com.example.dnd_manager.application.CharacterUseCases;
import com.example.dnd_manager.application.port.ScreenNavigator;
import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.lang.I18n;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Handles StartScreen navigation and flow decisions.
 */
public class StartScreenController {

    private static final Logger log = LoggerFactory.getLogger(StartScreenController.class);

    private final ScreenNavigator screenNavigator;
    private final CharacterUseCases characterUseCases;
    private final StartScreenFlowFactory screenFactory;
    private final Runnable openStartAction;
    private final BiConsumer<String, String> errorPresenter;

    public StartScreenController(
            ScreenNavigator screenNavigator,
            CharacterUseCases characterUseCases,
            StartScreenFlowFactory screenFactory,
            Runnable openStartAction,
            BiConsumer<String, String> errorPresenter
    ) {
        this.screenNavigator = screenNavigator;
        this.characterUseCases = characterUseCases;
        this.screenFactory = screenFactory;
        this.openStartAction = openStartAction;
        this.errorPresenter = errorPresenter;
    }

    public void openCharacterCreate() {
        log.info("Navigation: Opening Character Creation screen");
        screenNavigator.open(screenFactory.createCharacterCreate(openStartAction));
    }

    public void openCharacterEdit() {
        List<String> names = characterUseCases.listCharacterNamesUseCase().execute();
        if (names.isEmpty()) {
            errorPresenter.accept(I18n.t("error.no_characters_title"), I18n.t("error.no_characters_msg"));
            return;
        }

        Parent selectionScreen = buildSelectionScreen(true, character -> {
            Parent editScreen = screenFactory.createCharacterEdit(character, openStartAction);
            screenNavigator.open(editScreen);
        });
        screenNavigator.open(selectionScreen);
    }

    public void openCharacterLoad() {
        log.info("Navigation: Opening Character Selection (Load/View mode)");
        List<String> names = characterUseCases.listCharacterNamesUseCase().execute();
        if (names.isEmpty()) {
            log.warn("Navigation failed: No characters available for loading");
            errorPresenter.accept(I18n.t("error.no_characters_title"), I18n.t("error.no_characters_msg"));
            return;
        }

        Parent selectionScreen = buildSelectionScreen(false, character -> {
            log.info("Character selected for view: {}", character.getName());
            Parent overviewScreen = screenFactory.createCharacterOverview(character, openStartAction);
            screenNavigator.open(overviewScreen);
        });

        screenNavigator.open(selectionScreen);
    }

    public void openCharacterTransfer() {
        log.info("Navigation: Opening Import/Export screen");
        Parent screen = screenFactory.createCharacterTransfer(openStartAction);
        screenNavigator.open(screen);
    }

    public void openAssetManager() {
        Parent assetScreen = screenFactory.createAssetManager(openStartAction);
        screenNavigator.open(assetScreen);
    }

    public void changeLanguageAndReload() {
        Locale newLocale = I18n.isEnglish() ? Locale.forLanguageTag("ru") : Locale.ENGLISH;
        log.info("UI: Changing language to {}", newLocale);
        I18n.setLocale(newLocale);
        openStartAction.run();
    }

    private Parent buildSelectionScreen(boolean isEdit, Consumer<Character> onSelected) {
        return screenFactory.createCharacterSelection(isEdit, onSelected, openStartAction);
    }
}













