package com.example.dnd_manager.info.buff_debuff.model;

import com.example.dnd_manager.info.inventory.model.InventoryItem;

/**
 * Wrapper to hold a buff and its origin (Innate or Item).
 */
public record BuffWithSource(Buff buff, InventoryItem sourceItem) {}












