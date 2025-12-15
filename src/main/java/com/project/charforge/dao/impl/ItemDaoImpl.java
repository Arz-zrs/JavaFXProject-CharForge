package com.project.charforge.dao.impl;

import com.project.charforge.dao.base.BaseDao;
import com.project.charforge.dao.interfaces.ItemDao;
import com.project.charforge.db.ConnectionProvider;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class ItemDaoImpl extends BaseDao<Item> implements ItemDao {
    public ItemDaoImpl(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public List<Item> findAll() {
        return queryList("SELECT * FROM items", StatementBinder.empty());
    }

    @Override
    public Optional<Item> findById(int itemId) {
        String sql = "SELECT * FROM items WHERE id = ?";

        Item item = querySingle(
                sql,
                statement -> statement.setInt(1, itemId)
        );
        return Optional.ofNullable(item);
    }

    @Override
    protected Item mapRow(ResultSet result) throws SQLException {
        return new Item(
                result.getInt("id"),
                result.getString("name"),
                result.getDouble("weight"),
                EquipmentSlot.valueOf(result.getString("type").trim().toUpperCase()),
                result.getInt("stat_str"),
                result.getInt("stat_dex"),
                result.getInt("stat_int"),
                result.getInt("stat_hp"),
                result.getInt("stat_ap"),
                result.getInt("stat_atk"),
                result.getString("icon_path")
        );
    }
}
