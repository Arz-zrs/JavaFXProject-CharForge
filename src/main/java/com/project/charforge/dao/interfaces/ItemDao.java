package com.project.charforge.dao.interfaces;

import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;

import java.util.List;

public interface ItemDao {
    List<Item> findAll();
    List<Item> findBySlot(EquipmentSlot slot);
}
