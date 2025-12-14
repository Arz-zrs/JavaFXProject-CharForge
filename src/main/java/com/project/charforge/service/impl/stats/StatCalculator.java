package com.project.charforge.service.impl.stats;

import com.project.charforge.model.dto.DerivedStat;
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

        int baseStr = character.getRace().getStrBonus() + character.getCharClass().getStrBonus();
        int baseDex = character.getRace().getDexBonus() + character.getCharClass().getDexBonus();
        int baseInt = character.getRace().getIntBonus() + character.getCharClass().getIntBonus();

        int bonusStr = 0;
        int bonusDex = 0;
        int bonusInt = 0;

        int itemHpBonus = 0;
        int itemAtkBonus = 0;
        int itemApBonus = 0;

        for (InventoryItem inv : character.getInventory()) {
            if (!inv.isEquipped()) continue;

            bonusStr += inv.getItem().getStrBonus();
            bonusDex += inv.getItem().getDexBonus();
            bonusInt += inv.getItem().getIntBonus();

            itemHpBonus += inv.getItem().getHpBonus();
            itemAtkBonus += inv.getItem().getAtkBonus();
            itemApBonus += inv.getItem().getApBonus();
        }

        DerivedStat str = new DerivedStat(baseStr, bonusStr);
        DerivedStat dex = new DerivedStat(baseDex, bonusDex);
        DerivedStat ints = new DerivedStat(baseInt, bonusInt);

        int baseHp = BASE_HP + (str.base() * HP_PER_STR);
        int bonusHp = (str.bonus() * HP_PER_STR) + itemHpBonus;
        DerivedStat hp = new DerivedStat(baseHp, bonusHp);

        int scalingBase = resolveAttackScalingStat(
                character.getCharClass(),
                str.base(),
                dex.base(),
                ints.base()
        );
        int scalingBonus = resolveAttackScalingStat(
                character.getCharClass(),
                str.bonus(),
                dex.bonus(),
                ints.bonus()
        );
        DerivedStat atk = new DerivedStat(
                scalingBase * ATK_PER_STAT,
                (scalingBonus * ATK_PER_STAT) + itemAtkBonus
        );

        DerivedStat ap = new DerivedStat(
                dex.base() * AP_PER_DEX,
                (dex.bonus() * AP_PER_DEX) + itemApBonus
        );

        double currentWeight = encumbranceService.getCurrentWeight(character);
        double maxWeight = encumbranceService.getMaxWeight(character, str.base());
        boolean encumbered = currentWeight > maxWeight;

        return new StatSnapshot(
                str,
                dex,
                ints,
                hp,
                ap,
                atk,
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