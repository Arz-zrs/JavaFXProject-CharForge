package com.project.charforge.model.service;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;

public class ValidationService {
    public boolean canEquip(PlayerCharacter character, Item item, EquipmentSlot targetSlot) {
        if (item.getValidSlot() != targetSlot) return false;
        return true;
    }
}
