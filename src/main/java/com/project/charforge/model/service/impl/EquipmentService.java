package com.project.charforge.model.service.impl;

import com.project.charforge.dao.interfaces.InventoryDao;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.service.interfaces.IEquipmentService;
import com.project.charforge.model.service.interfaces.IValidationService;

import java.util.List;
import java.util.Optional;

public class EquipmentService implements IEquipmentService {
    private final InventoryDao inventoryDao;
    private final IValidationService validationService;

    public EquipmentService(InventoryDao inventoryDao, IValidationService validationService) {
        this.inventoryDao = inventoryDao;
        this.validationService = validationService;
    }


    @Override
    public List<InventoryItem> loadInventory(PlayerCharacter character) {
        List<InventoryItem> items = inventoryDao.findByCharacterId(character.getId());
        character.getInventory().clear();
        character.getInventory().addAll(items);
        return items;
    }

    @Override
    public void equip(PlayerCharacter character, int instanceId, EquipmentSlot slot) {
        InventoryItem newItem = findInventoryItem(character, instanceId);

        if (!validationService.canEquip(character, newItem, slot)) {
            throw new IllegalStateException("Cannot equip item "+ newItem.getItem().getName() +" into " + slot);
        }

        // Check if current item is currently used
        Optional<InventoryItem> currentEquipped = character.getInventory().stream()
                .filter(i -> i.isEquipped() && slot.name().equals(i.getSlotName()))
                .findFirst();

        if (currentEquipped.isPresent()) {
            InventoryItem oldItem = currentEquipped.get();
            if (oldItem.getInstanceId() == newItem.getInstanceId()) return;
            unequip(character, oldItem.getInstanceId());
        }

        inventoryDao.equipItem(instanceId, slot.name());
    }

    @Override
    public void unequip(PlayerCharacter character, int instanceId) {
        // Search empty slot in grid
        long itemsInBag =
                character.getInventory().stream().filter(i -> !i.isEquipped()).count();
        int newGridIndex = (int) itemsInBag;

        inventoryDao.unequipItem(instanceId, newGridIndex);
    }

    @Override
    public boolean canEquip(PlayerCharacter character, int instanceId, EquipmentSlot targetSlot) {
        InventoryItem item = character.getInventory()
                .stream()
                .filter(i -> i.getInstanceId() == instanceId)
                .findFirst()
                .orElse(null);

        if (item == null) return false;

        return validationService.canEquip(character, item, targetSlot);
    }

    private InventoryItem findInventoryItem(PlayerCharacter character, int instanceId) {
        return character.getInventory().stream()
                .filter(i -> i.getInstanceId() == instanceId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Item not found in character inventory: " + instanceId));
    }
}
