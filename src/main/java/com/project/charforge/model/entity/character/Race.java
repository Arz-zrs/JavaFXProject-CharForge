package com.project.charforge.model.entity.character;

import com.project.charforge.model.entity.base.BaseEntity;
import com.project.charforge.model.entity.base.StatModifier;

public class Race extends BaseEntity implements StatModifier {
    private final int baseStr;
    private final int baseDex;
    private final int baseInt;
    private final double weightModifier;

    public Race(int id, String name, int baseStr, int baseDex, int baseInt, double weighModifier) {
        super(id, name);
        this.baseStr = baseStr;
        this.baseDex = baseDex;
        this.baseInt = baseInt;
        this.weightModifier = weighModifier;
    }

    @Override public int getStrBonus() { return baseStr; }
    @Override public int getDexBonus() { return baseDex; }
    @Override public int getIntBonus() { return baseInt; }
    public double getWeightModifier() { return weightModifier; }

    public String describe() {
        return String.format("RACE: %s\nStr: +%d | Dex: +%d | Int: +%d | Weight Modifier: %.2f",
                getName(), getStrBonus(), getDexBonus(), getIntBonus(), getWeightModifier());
    }
}
