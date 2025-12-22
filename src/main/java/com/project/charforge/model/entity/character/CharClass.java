package com.project.charforge.model.entity.character;

import com.project.charforge.model.entity.base.BaseEntity;
import com.project.charforge.model.entity.base.StatModifier;

public class CharClass extends BaseEntity implements StatModifier {
    private static final int HP_FROM_STR = 5;

    private final int bonusStr;
    private final int bonusDex;
    private final int bonusInt;
    private final AttackScaling attackScaling;

    public CharClass(int id, String name, int bonusStr,
                     int bonusDex, int bonusInt, AttackScaling attackScaling)
    {
        super(id, name);
        this.bonusStr = bonusStr;
        this.bonusDex = bonusDex;
        this.bonusInt = bonusInt;
        this.attackScaling = attackScaling;
    }

    public AttackScaling getAttackScaling() {
        return attackScaling;
    }

    @Override public int getStrBonus() { return bonusStr; }
    @Override public int getDexBonus() { return bonusDex; }
    @Override public int getIntBonus() { return bonusInt; }

    public String describe() {
        return String.format("CLASS: %s\nStr: +%d | Dex: +%d | Int: +%d | HP bonus: +%d",
                getName(), getStrBonus(), getDexBonus(), getIntBonus(), getStrBonus() * HP_FROM_STR);
    }
}
