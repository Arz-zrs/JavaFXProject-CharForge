package com.project.charforge.model.entity.character;

import com.project.charforge.model.entity.base.BaseEntity;
import com.project.charforge.model.entity.base.StatModifier;

public class CharClass extends BaseEntity implements StatModifier {
    private final int bonusStr;
    private final int bonusDex;
    private final int bonusInt;

    public CharClass(int id, String name, int bonusStr, int bonusDex, int bonusInt) {
        super(id, name);
        this.bonusStr = bonusStr;
        this.bonusDex = bonusDex;
        this.bonusInt = bonusInt;
    }

    @Override public int getStrBonus() { return bonusStr; }
    @Override public int getDexBonus() { return bonusDex; }
    @Override public int getIntBonus() { return bonusInt; }
}
