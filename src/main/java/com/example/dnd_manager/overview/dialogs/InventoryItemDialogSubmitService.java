package com.example.dnd_manager.overview.dialogs;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.Buff;
import com.example.dnd_manager.info.inventory.InventoryItem;
import com.example.dnd_manager.info.skills.Skill;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class InventoryItemDialogSubmitService {

    private final InventoryItemMutationService mutationService;
    private final String defaultIconPath;

    public InventoryItemDialogSubmitService(InventoryItemMutationService mutationService, String defaultIconPath) {
        this.mutationService = Objects.requireNonNull(mutationService, "mutationService must not be null");
        this.defaultIconPath = Objects.requireNonNull(defaultIconPath, "defaultIconPath must not be null");
    }

    public InventoryItem submit(
            Character character,
            InventoryItem existingItem,
            InventoryItemFormInput input,
            List<Buff> attachedBuffs,
            List<Skill> attachedSkills,
            Consumer<InventoryItem> onComplete
    ) {
        Objects.requireNonNull(character, "character must not be null");
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(attachedBuffs, "attachedBuffs must not be null");
        Objects.requireNonNull(attachedSkills, "attachedSkills must not be null");
        Objects.requireNonNull(onComplete, "onComplete must not be null");

        if (existingItem != null) {
            mutationService.applyToExisting(
                    existingItem,
                    input.name(),
                    input.description(),
                    input.count(),
                    input.iconPath(),
                    defaultIconPath,
                    input.equipped(),
                    input.customEffectName(),
                    attachedBuffs,
                    attachedSkills
            );
            onComplete.accept(existingItem);
            return existingItem;
        }

        InventoryItem item = mutationService.createNew(
                input.name(),
                input.description(),
                input.count(),
                input.iconPath(),
                defaultIconPath,
                input.equipped(),
                input.customEffectName(),
                attachedBuffs,
                attachedSkills
        );
        character.getInventory().add(item);
        onComplete.accept(item);
        return item;
    }
}

