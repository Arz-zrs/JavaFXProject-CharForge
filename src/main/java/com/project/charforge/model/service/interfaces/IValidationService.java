package com.project.charforge.model.service.interfaces;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;

public interface IValidationService {
    boolean canEquip(PlayerCharacter character, InventoryItem inventoryItem, EquipmentSlot targetSlot);
}
