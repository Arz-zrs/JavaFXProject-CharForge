package com.project.charforge.model.service.interfaces;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;

import java.util.List;

public interface IEquipmentService {
    List<InventoryItem> loadInventory(PlayerCharacter character);
    boolean canEquip(PlayerCharacter character, int instanceId, EquipmentSlot targetSlot);
    void equip(PlayerCharacter character, int instanceId, EquipmentSlot slot);
    void unequip(PlayerCharacter character, int instanceId);
    void addStartingEquipment(int characterId, String className);
}
