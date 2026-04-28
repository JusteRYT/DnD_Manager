package com.example.dnd_manager.info.editors.common;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EditorItemMutationServiceTest {

    private final EditorItemMutationService service = new EditorItemMutationService();

    @Test
    void addOrReplace_addsWhenEditingItemIsNull() {
        List<String> items = new ArrayList<>();

        service.addOrReplace(items, null, "new");

        assertEquals(List.of("new"), items);
    }

    @Test
    void addOrReplace_replacesExistingItem() {
        List<String> items = new ArrayList<>(List.of("old", "keep"));

        service.addOrReplace(items, "old", "new");

        assertEquals(List.of("new", "keep"), items);
    }

    @Test
    void addOrReplace_doesNothingWhenEditedItemIsMissing() {
        List<String> items = new ArrayList<>(List.of("keep"));

        service.addOrReplace(items, "missing", "new");

        assertEquals(List.of("keep"), items);
    }
}













