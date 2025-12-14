package com.project.charforge.service.interfaces.items;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;

import java.util.List;

public interface IInventoryService {
    List<InventoryItem> getInventory(PlayerCharacter character);
    void addItem(PlayerCharacter character, int itemId);
    void addTempItem(PlayerCharacter character, int itemId);
}
