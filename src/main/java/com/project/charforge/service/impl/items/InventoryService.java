package com.project.charforge.service.impl.items;

import com.project.charforge.dao.interfaces.InventoryDao;
import com.project.charforge.dao.interfaces.ItemDao;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.Item;
import com.project.charforge.service.interfaces.items.IInventoryService;
import com.project.charforge.utils.AlertUtils;

import java.util.List;

public class InventoryService implements IInventoryService {
    private static final int TEMP_CHARACTER_ID = 0;

    private final InventoryDao inventoryDao;
    private final ItemDao itemDao;

    public InventoryService(InventoryDao inventoryDao, ItemDao itemDao) {
        this.inventoryDao = inventoryDao;
        this.itemDao = itemDao;
    }

    @Override
    public List<InventoryItem> getInventory(PlayerCharacter character) {
        if (character.getId() != TEMP_CHARACTER_ID) {
            List<InventoryItem> dbItems = inventoryDao.findByCharacterId(character.getId());
            character.setInventory(dbItems);
        }
        return character.getInventory();
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

    @Override
    public void addTempItem(PlayerCharacter character, int itemId) {
        Item itemMaster = itemDao.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item ID not found: " + itemId));

        int nextIndex = character.getInventory().size();

        InventoryItem newItem = new InventoryItem(0, itemMaster, null, nextIndex);

        character.getInventory().add(newItem);
    }

    @Override
    public void removeItem(PlayerCharacter character, InventoryItem item) {
        if (character.getId() != 0) {
            boolean success = inventoryDao.deleteItem(item.getInstanceId());
            if (!success) AlertUtils.showError("Error", "Failed to remove item");
        }
        character.getInventory().remove(item);
    }
}
