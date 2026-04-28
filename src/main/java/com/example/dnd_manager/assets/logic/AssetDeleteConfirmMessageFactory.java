package com.example.dnd_manager.assets.logic;

import com.example.dnd_manager.lang.I18n;

import java.nio.file.Path;
import java.util.Set;

public class AssetDeleteConfirmMessageFactory {

    public String create(Set<Path> targets) {
        if (targets == null || targets.isEmpty()) {
            return "";
        }

        if (targets.size() == 1) {
            String fileName = targets.iterator().next().getFileName().toString();
            return String.format(I18n.t("asset.delete.confirm.single"), fileName);
        }

        return String.format(I18n.t("asset.delete.confirm.multiple"), targets.size());
    }
}












