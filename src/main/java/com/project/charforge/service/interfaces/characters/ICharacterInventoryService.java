package com.project.charforge.service.interfaces.characters;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;

import java.util.List;

public interface ICharacterInventoryService {
    List<InventoryItem> getInventory(PlayerCharacter character);
    void addItem(PlayerCharacter character, int itemId);
    void removeItem(PlayerCharacter character, int inventoryIndex);
}
