package com.project.charforge.model.entity.inventory;

import com.project.charforge.model.entity.item.Item;

public class InventoryItem {
    private final int instanceId;
    private final Item item;
    private String slotName;
    private int gridIndex;

    public InventoryItem(int instanceId, Item item, String slotName, int gridIndex) {
        this.instanceId = instanceId;
        this.item = item;
        this.slotName = slotName;
        this.gridIndex = gridIndex;
    }

    public boolean isEquipped() {
        return slotName != null && !slotName.isEmpty();
    }

    public int getInstanceId() {
        return instanceId;
    }

    public Item getItem() {
        return item;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public int getGridIndex() {
        return gridIndex;
    }

    public void setGridIndex(int gridIndex) {
        this.gridIndex = gridIndex;
    }
}
