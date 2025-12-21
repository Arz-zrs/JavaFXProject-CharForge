package com.project.charforge.service.impl.characters;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.service.interfaces.characters.ICharacterInventoryService;
import com.project.charforge.service.interfaces.items.IInventoryService;

import java.util.List;

public class CharacterInventoryService implements ICharacterInventoryService {
    private static final int TEMP_CHARACTER_ID = 0;

    private final IInventoryService inventoryService;

    public CharacterInventoryService(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    public List<InventoryItem> getInventory(PlayerCharacter character) {
        return inventoryService.getInventory(character);
    }

    @Override
    public void addItem(PlayerCharacter character, int itemId) {
        if (character.getId() == TEMP_CHARACTER_ID) {
            inventoryService.addTempItem(character, itemId);
        } else {
            inventoryService.addItem(character, itemId);
        }
    }

    @Override
    public void removeItem(PlayerCharacter character, int inventoryIndex) {
        if (inventoryIndex < 0 || inventoryIndex >= character.getInventory().size()) {
            throw new IllegalArgumentException("Invalid inventory index");
        }

        InventoryItem item = character.getInventory().get(inventoryIndex);
        inventoryService.removeItem(character, item);
    }
}
