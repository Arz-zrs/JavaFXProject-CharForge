package com.project.charforge.service.impl;

import com.project.charforge.dao.interfaces.InventoryDao;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.service.interfaces.IEquipmentService;
import com.project.charforge.service.interfaces.IValidationService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        int newGridIndex = findFreeGridIndex(character);
        inventoryDao.unequipItem(instanceId, newGridIndex);
    }

    private int findFreeGridIndex(PlayerCharacter character) {
        Set<Integer> occupiedIndices = character.getInventory().stream()
                .filter(i -> !i.isEquipped())
                .map(InventoryItem::getGridIndex)
                .collect(Collectors.toSet());

        int candidate = 0;
        while (occupiedIndices.contains(candidate)) {
            candidate++;
        }
        return candidate;
    }

    @Override
    public void addStartingEquipment(int characterId, String className) {
        // Add leather cap to all class
        inventoryDao.addItemToCharacter(characterId, 2, 0);

        // Add weapons respective to class: Warrior -> Sword, Mage -> Staff, Archer -> dagger
        if ("Warrior".equalsIgnoreCase(className)) inventoryDao.addItemToCharacter(characterId, 1, 1);
        else if ("Mage".equalsIgnoreCase(className)) inventoryDao.addItemToCharacter(characterId, 3, 1);
        else if ("Archer".equalsIgnoreCase(className)) inventoryDao.addItemToCharacter(characterId, 4, 1);
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
