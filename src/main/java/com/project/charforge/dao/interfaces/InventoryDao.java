package com.project.charforge.dao.interfaces;

import com.project.charforge.model.entity.inventory.InventoryItem;

import java.util.List;

public interface InventoryDao {
    List<InventoryItem> findByCharacterId(int charId);
    void equipItem(int instanceId, String slotName);
    void unequipItem(int instanceId, int newGridIndex);
    void addItemToCharacter(int charId, int itemId, int gridIndex);
}
