package com.project.charforge.model.service.impl;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;
import com.project.charforge.model.service.interfaces.IValidationService;

public class ValidationService implements IValidationService {
    @Override
    public boolean canEquip(PlayerCharacter character, InventoryItem inventoryItem, EquipmentSlot targetSlot) {
        Item item = inventoryItem.getItem();

        // Items in MISC category cannot be equipped
        if (item.getValidSlot() == EquipmentSlot.MISC) {
            return false;
        }
        // Return slot validity value
        return item.getValidSlot() == targetSlot;
    }
}
