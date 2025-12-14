package com.project.charforge.model.entity.item;

import com.project.charforge.model.entity.base.BaseEntity;
import com.project.charforge.model.entity.base.StatModifier;

public class Item extends BaseEntity implements StatModifier {
    private final double weight;
    private final EquipmentSlot validSlot;
    private final int strength;
    private final int dexterity;
    private final int intelligence;
    private final int hpBonus;
    private final int apBonus;
    private final int atkBonus;
    private final String iconPath;

    public Item(int id, String name, double weight,
                EquipmentSlot validSlot, int strength, int dexterity,
                int intelligence, int hp, int ap,
                int atk, String iconPath)
    {
        super(id, name);
        this.weight = weight;
        this.validSlot = validSlot;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.hpBonus = hp;
        this.apBonus = ap;
        this.atkBonus = atk;
        this.iconPath = iconPath;
    }

    public int getHpBonus() { return hpBonus; }
    public int getApBonus() { return apBonus; }
    public int getAtkBonus() { return atkBonus; }

    public double getWeight() { return weight; }
    public EquipmentSlot getValidSlot() { return validSlot; }
    public String getIconPath() { return iconPath; }

    @Override public int getStrBonus() { return strength; }
    @Override public int getDexBonus() { return dexterity; }
    @Override public int getIntBonus() { return intelligence; }
}
