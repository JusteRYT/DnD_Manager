package com.example.dnd_manager.screen.start;

import com.example.dnd_manager.domain.Character;
import javafx.scene.Parent;

import java.util.function.Consumer;

/**
 * Factory for building feature screens opened from StartScreen flow.
 */
public interface StartScreenFlowFactory {

    Parent createCharacterCreate(Runnable backToStartAction);

    Parent createCharacterEdit(Character character, Runnable backToStartAction);

    Parent createCharacterOverview(Character character, Runnable backToStartAction);

    Parent createCharacterSelection(
            boolean isEdit,
            Consumer<Character> onCharacterSelected,
            Runnable backToStartAction
    );

    Parent createCharacterTransfer(Runnable backToStartAction);

    Parent createAssetManager(Runnable backToStartAction);
}














