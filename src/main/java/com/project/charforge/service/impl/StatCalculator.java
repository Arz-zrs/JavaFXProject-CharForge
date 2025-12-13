package com.project.charforge.service.impl;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.dto.StatSnapshot;
import com.project.charforge.service.interfaces.IStatCalculator;

public class StatCalculator implements IStatCalculator {

    @Override
    public StatSnapshot calculate(PlayerCharacter character) {

        int str = character.getRace().getStrBonus() + character.getCharClass().getStrBonus();
        int dex = character.getRace().getDexBonus() + character.getCharClass().getDexBonus();
        int intel = character.getRace().getIntBonus() + character.getCharClass().getIntBonus();

        double currentWeight = 0.0;

        for (InventoryItem invItem : character.getInventory()) {
            currentWeight += invItem.getItem().getWeight();

            if (invItem.isEquipped()) {
                str += invItem.getItem().getStrBonus();
                dex += invItem.getItem().getDexBonus();
                intel += invItem.getItem().getIntBonus();
            }
        }

        double baseCap = 50.0;
        double maxWeight = (baseCap + (str * 2)) * character.getRace().getWeightModifier();

        return new StatSnapshot(str, dex, intel, currentWeight, maxWeight, currentWeight > maxWeight);
    }
}