package com.example.dnd_manager.info.editors.common;

import java.util.List;

public class EditorItemMutationService {

    public <T> void addOrReplace(List<T> items, T editingItem, T nextItem) {
        if (editingItem == null) {
            items.add(nextItem);
            return;
        }

        int index = items.indexOf(editingItem);
        if (index != -1) {
            items.set(index, nextItem);
        }
    }
}













