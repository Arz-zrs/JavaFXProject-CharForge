package com.project.charforge.dao.interfaces;

import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    List<Item> findAll();
    Optional<Item> findById(int itemId);
}
