package com.example.dnd_manager.overview.dialogs.inventory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryItemCountResolverTest {

    private final InventoryItemCountResolver resolver = new InventoryItemCountResolver();

    @Test
    void resolve_returnsParsedInteger() {
        assertEquals(12, resolver.resolve("12"));
    }

    @Test
    void resolve_returnsOneForBlankOrInvalid() {
        assertEquals(1, resolver.resolve(""));
        assertEquals(1, resolver.resolve("abc"));
        assertEquals(1, resolver.resolve(null));
    }
}













