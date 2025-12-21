package com.project.charforge.service.impl.items;

import com.project.charforge.dao.interfaces.InventoryDao;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.service.interfaces.items.IEquipmentService;
import com.project.charforge.service.interfaces.utils.IMessageService;
import com.project.charforge.service.interfaces.utils.IValidationService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EquipmentService implements IEquipmentService {
    private final InventoryDao inventoryDao;
    private final IValidationService validationService;
    private final IMessageService message;

    public EquipmentService(
            InventoryDao inventoryDao,
            IValidationService validationService,
            IMessageService message
    ) {
        this.inventoryDao = inventoryDao;
        this.validationService = validationService;
        this.message = message;
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

        boolean success = inventoryDao.equipItem(instanceId, slot.name());
        if (!success) message.error(
                "Error: " + instanceId + " " + slot.name(),
                "Failed to equip item: " + newItem
        );

    }

    private InventoryItem findInventoryItem(PlayerCharacter character, int instanceId) {
        return character.getInventory().stream()
                .filter(i -> i.getInstanceId() == instanceId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Item not found in character inventory: " + instanceId));
    }

    @Override
    public void unequip(PlayerCharacter character, int instanceId) {
        int newGridIndex = findFreeGridIndex(character);
        boolean success = inventoryDao.unequipItem(instanceId, newGridIndex);
        if (!success) message.error(
                "Error: " + instanceId + " " + newGridIndex,
                "Failed to unequip item"
        );
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
    public boolean canEquip(PlayerCharacter character, int instanceId, EquipmentSlot targetSlot) {
        InventoryItem item = character.getInventory()
                .stream()
                .filter(i -> i.getInstanceId() == instanceId)
                .findFirst()
                .orElse(null);

        if (item == null) return false;

        return validationService.canEquip(character, item, targetSlot);
    }
}
