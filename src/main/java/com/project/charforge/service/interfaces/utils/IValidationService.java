package com.project.charforge.service.interfaces.utils;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;

public interface IValidationService {
    boolean canEquip(PlayerCharacter character, InventoryItem inventoryItem, EquipmentSlot targetSlot);
}
