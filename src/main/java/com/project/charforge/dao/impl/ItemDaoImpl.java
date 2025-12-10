package com.project.charforge.dao.impl;

import com.project.charforge.dao.base.BaseDao;
import com.project.charforge.dao.interfaces.ItemDao;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;

import java.sql.*;
import java.util.List;

public class ItemDaoImpl extends BaseDao<Item> implements ItemDao {
    @Override
    public List<Item> findAll() {
        return queryList("SELECT * FROM classes", StatementBinder.empty());
    }

    @Override
    public List<Item> findBySlot(EquipmentSlot slot) {
        return queryList(
                "SELECT * FROM items WHERE type = ?",
                statement -> statement.setString(1, slot.name())
        );
    }

    @Override
    protected Item mapRow(ResultSet result) throws SQLException {
        return new Item(
                result.getInt("id"),
                result.getString("name"),
                result.getDouble("weight"),
                EquipmentSlot.valueOf(result.getString("type")),
                result.getInt("stat_str"),
                result.getInt("stat_dex"),
                result.getInt("stat_int"),
                result.getString("icon_path")
        );
    }
}
