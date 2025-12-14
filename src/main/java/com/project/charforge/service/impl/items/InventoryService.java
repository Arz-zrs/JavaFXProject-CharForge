package com.project.charforge.service.impl.items;

import com.project.charforge.dao.interfaces.InventoryDao;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.service.interfaces.items.IInventoryService;

import java.util.List;

public class InventoryService implements IInventoryService {
    private final InventoryDao inventoryDao;

    public InventoryService(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    @Override
    public List<InventoryItem> getInventory(PlayerCharacter character) {
        List<InventoryItem> inventory = inventoryDao.findByCharacterId(character.getId());

        character.setInventory(inventory);
        return inventory;
    }

    @Override
    public void addItem(PlayerCharacter character, int itemId) {
        int nextIndex = character.getInventory().size();

        inventoryDao.addItemToCharacter(
                character.getId(),
                itemId,
                nextIndex
        );

        List<InventoryItem> updatedInventory = inventoryDao.findByCharacterId(character.getId());
        character.setInventory(updatedInventory);
    }
}
