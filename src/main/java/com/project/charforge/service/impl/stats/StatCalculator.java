package com.project.charforge.service.impl.stats;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.dto.StatSnapshot;
import com.project.charforge.service.interfaces.stats.IStatCalculator;

public class StatCalculator implements IStatCalculator {

    @Override
    public StatSnapshot calculate(PlayerCharacter character) {
        int str = character.getRace().getStrBonus() + character.getCharClass().getStrBonus();

        int dex = character.getRace().getDexBonus() + character.getCharClass().getDexBonus();

        int ints = character.getRace().getIntBonus() + character.getCharClass().getIntBonus();

        for (InventoryItem inv : character.getInventory()) {
            if (inv.isEquipped()) {
                str += inv.getItem().getStrBonus();
                dex += inv.getItem().getDexBonus();
                ints += inv.getItem().getIntBonus();
            }
        }

        return new StatSnapshot(str, dex, ints);
    }
}