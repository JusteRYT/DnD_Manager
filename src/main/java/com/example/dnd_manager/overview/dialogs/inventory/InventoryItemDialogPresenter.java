package com.example.dnd_manager.overview.dialogs.inventory;

import com.example.dnd_manager.domain.Character;
import com.example.dnd_manager.info.buff_debuff.model.Buff;
import com.example.dnd_manager.info.inventory.model.InventoryItem;
import com.example.dnd_manager.info.skills.model.Skill;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class InventoryItemDialogPresenter {

    private final InventoryItemFormValidator validator;
    private final InventoryItemCountResolver countResolver;
    private final InventoryItemDialogSubmitService submitService;

    public InventoryItemDialogPresenter(
            InventoryItemFormValidator validator,
            InventoryItemCountResolver countResolver,
            InventoryItemDialogSubmitService submitService
    ) {
        this.validator = Objects.requireNonNull(validator, "validator must not be null");
        this.countResolver = Objects.requireNonNull(countResolver, "countResolver must not be null");
        this.submitService = Objects.requireNonNull(submitService, "submitService must not be null");
    }

    public boolean submit(
            Character character,
            InventoryItem existingItem,
            String name,
            String description,
            String countText,
            String iconPath,
            boolean equipped,
            String effectName,
            List<Buff> attachedBuffs,
            List<Skill> attachedSkills,
            Consumer<InventoryItem> onComplete
    ) {
        if (!validator.isNameValid(name)) {
            return false;
        }

        submitService.submit(
                character,
                existingItem,
                new InventoryItemFormInput(
                        name,
                        description,
                        countResolver.resolve(countText),
                        iconPath,
                        equipped,
                        effectName
                ),
                attachedBuffs,
                attachedSkills,
                onComplete
        );
        return true;
    }
}













