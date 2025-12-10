package com.project.charforge.model.entity.character;

import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;

import java.util.HashMap;
import java.util.Map;

public class PlayerCharacter {
    private Integer id;
    private String name;
    private Race race;
    private CharClass CharClass;
    private Map<EquipmentSlot, Item> equipment = new HashMap<>();

    public PlayerCharacter(){ }

    public CharClass getCharClass() { return CharClass; }
    public void setCharClass(CharClass CharClass) { this.CharClass = CharClass; }

    public Race getRace() { return race; }
    public void setRace(Race race) { this.race = race; }

    public Map<EquipmentSlot, Item> getEquipment() { return equipment;}
    public void equipItem(EquipmentSlot slot, Item item) { equipment.put(slot, item); }
    public void unequipItem(EquipmentSlot slot) { equipment.remove(slot); }
}
