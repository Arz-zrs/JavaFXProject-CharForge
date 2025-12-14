package com.project.charforge.service.impl.stats;

import com.project.charforge.model.entity.character.CharClass;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.dto.StatSnapshot;
import com.project.charforge.service.interfaces.stats.IEncumbranceService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;

public class StatCalculator implements IStatCalculator {
    private static final int BASE_HP = 100;
    private static final int HP_PER_STR = 5;
    private static final int ATK_PER_STAT = 2;
    private static final int AP_PER_DEX = 1;


    private final IEncumbranceService encumbranceService;

    public StatCalculator(IEncumbranceService encumbranceService) {
        this.encumbranceService = encumbranceService;
    }

    @Override
    public StatSnapshot calculate(PlayerCharacter character) {

        int str = character.getRace().getStrBonus() + character.getCharClass().getStrBonus();
        int dex = character.getRace().getDexBonus() + character.getCharClass().getDexBonus();
        int ints = character.getRace().getIntBonus() + character.getCharClass().getIntBonus();

        int itemHpBonus = 0;
        int itemAtkBonus = 0;
        int itemApBonus = 0;

        for (InventoryItem inv : character.getInventory()) {
            if (!inv.isEquipped()) continue;

            str += inv.getItem().getStrBonus();
            dex += inv.getItem().getDexBonus();
            ints += inv.getItem().getIntBonus();

            itemHpBonus += inv.getItem().getHpBonus();
            itemAtkBonus += inv.getItem().getAtkBonus();
            itemApBonus += inv.getItem().getApBonus();
        }

        int attackScalingStat = resolveAttackScalingStat(
                character.getCharClass(),
                str,
                dex,
                ints
        );

        int maxHp = BASE_HP + (str * HP_PER_STR) + itemHpBonus;
        int totalAtk = (attackScalingStat * ATK_PER_STAT) + itemAtkBonus;
        int totalAp = (dex * AP_PER_DEX) + itemApBonus;

        double currentWeight = encumbranceService.getCurrentWeight(character);
        double maxWeight = encumbranceService.getMaxWeight(character, str);
        boolean encumbered = currentWeight > maxWeight;

        return new StatSnapshot(
                str,
                dex,
                ints,
                maxHp,
                totalAp,
                totalAtk,
                currentWeight,
                maxWeight,
                encumbered
        );
    }

    private int resolveAttackScalingStat(CharClass charClass, int str, int dex, int ints) {
        return switch (charClass.getAttackScaling()) {
            case STR -> str;
            case DEX -> dex;
            case INT -> ints;
        };
    }
}