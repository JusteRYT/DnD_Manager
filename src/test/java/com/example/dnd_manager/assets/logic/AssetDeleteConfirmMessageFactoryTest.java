package com.example.dnd_manager.assets.logic;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AssetDeleteConfirmMessageFactoryTest {

    private final AssetDeleteConfirmMessageFactory factory = new AssetDeleteConfirmMessageFactory();

    @Test
    void create_returnsSingleFileMessage() {
        String text = factory.create(Set.of(Path.of("Assets", "Items", "sword.png")));

        assertTrue(text.contains("sword.png"));
    }

    @Test
    void create_returnsMultipleFilesMessage() {
        String text = factory.create(Set.of(
                Path.of("Assets", "Items", "sword.png"),
                Path.of("Assets", "Items", "shield.png")
        ));

        assertTrue(text.contains("2"));
    }
}
