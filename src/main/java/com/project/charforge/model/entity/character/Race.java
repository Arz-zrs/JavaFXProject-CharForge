package com.project.charforge.model.entity.character;

import com.project.charforge.model.entity.base.BaseEntity;
import com.project.charforge.model.entity.base.StatModifier;

public class Race extends BaseEntity implements StatModifier {
    private static final int HP_FROM_STR = 5;

    private final int baseHp;
    private final int baseStr;
    private final int baseDex;
    private final int baseInt;
    private final double weightModifier;

    public Race(int id, String name, int baseHp, int baseStr, int baseDex, int baseInt, double weighModifier) {
        super(id, name);
        this.baseHp = baseHp;
        this.baseStr = baseStr;
        this.baseDex = baseDex;
        this.baseInt = baseInt;
        this.weightModifier = weighModifier;
    }

    @Override public int getStrBonus() { return baseStr; }
    @Override public int getDexBonus() { return baseDex; }
    @Override public int getIntBonus() { return baseInt; }
    public double getWeightModifier() { return weightModifier; }
    public int getBaseHp() { return baseHp; }

    public String describe() {
        int hpFromStr = baseStr * HP_FROM_STR;
        int totalRaceHp = baseHp + hpFromStr;

        return String.format(
                """
                RACE: %s
                Health Points: %d (Base %d + Str Bonus %d)
                Str: +%d | Dex: +%d | Int: +%d
                Weight Cap Modifier: x%.2f
                """,
                getName(), totalRaceHp, baseHp, hpFromStr, baseStr, baseDex, baseInt, weightModifier
        );
    }
}
