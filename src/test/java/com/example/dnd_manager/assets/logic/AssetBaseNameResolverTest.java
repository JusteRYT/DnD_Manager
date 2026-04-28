package com.example.dnd_manager.assets.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssetBaseNameResolverTest {

    private final AssetBaseNameResolver resolver = new AssetBaseNameResolver();

    @Test
    void resolve_returnsNameWithoutExtension() {
        assertEquals("sword", resolver.resolve("sword.png"));
    }

    @Test
    void resolve_returnsSameWhenNoExtension() {
        assertEquals("sword", resolver.resolve("sword"));
    }

    @Test
    void resolve_keepsHiddenFileName() {
        assertEquals(".hidden", resolver.resolve(".hidden"));
    }
}












