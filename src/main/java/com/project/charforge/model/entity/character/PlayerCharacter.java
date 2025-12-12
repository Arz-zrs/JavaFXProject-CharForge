package com.project.charforge.model.entity.character;

import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerCharacter {
    private Integer id;
    private String name;
    private Race race;
    private CharClass CharClass;
    private Gender gender;
    private Map<EquipmentSlot, Item> equipment = new HashMap<>();
    private List<InventoryItem> inventory = new ArrayList<>();

    public PlayerCharacter(){ }

    public CharClass getCharClass() {
        return CharClass;
    }

    public void setCharClass(CharClass CharClass) {
        this.CharClass = CharClass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setEquipment(Map<EquipmentSlot, Item> equipment) {
        this.equipment = equipment;
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    public void setInventory(List<InventoryItem> inventory) {
        this.inventory = inventory;
    }

    public Map<EquipmentSlot, Item> getEquipment() {
        return equipment;
    }
    public void equipItem(EquipmentSlot slot, Item item) {
        equipment.put(slot, item);
    }
    public void unequipItem(EquipmentSlot slot) {
        equipment.remove(slot);
    }

    public Map<EquipmentSlot, Item> getEquippedItemsMap(){
        Map<EquipmentSlot, Item> equippedMap = new HashMap<>();
        for (InventoryItem inventoryItem: inventory) {
            if (inventoryItem.isEquipped()) {
                // Error handling if string slot in Database is invalid
                try {
                    EquipmentSlot slot = EquipmentSlot.valueOf(inventoryItem.getSlotName());
                    equippedMap.put(slot, inventoryItem.getItem());
                } catch (IllegalArgumentException e) {
                    // TODO:
                    // alert message, breaks SOLID principle if I call Alert.AlertType.ERROR
                    // System.err.println() would work as well
                    throw new RuntimeException(e);
                }
            }
        }
        return equippedMap;
    }
}
