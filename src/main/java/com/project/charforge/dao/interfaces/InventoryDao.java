package com.project.charforge.dao.interfaces;

import com.project.charforge.model.entity.inventory.InventoryItem;

import java.util.List;

public interface InventoryDao {
    List<InventoryItem> findByCharacterId(int charId);
    boolean equipItem(int instanceId, String slotName);
    boolean unequipItem(int instanceId, int newGridIndex);
    void addItemToCharacter(int charId, int itemId, int gridIndex);
    boolean deleteItem(int instanceId);
}
