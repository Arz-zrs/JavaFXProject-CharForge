package com.project.charforge.dao.impl;

import com.project.charforge.dao.base.BaseDao;
import com.project.charforge.dao.interfaces.InventoryDao;
import com.project.charforge.db.ConnectionProvider;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InventoryDaoImpl extends BaseDao<InventoryItem> implements InventoryDao {
    public InventoryDaoImpl(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public List<InventoryItem> findByCharacterId(int charId) {
        String sql = """
        SELECT
            ci.*,
            i.id AS item_id,
            i.name AS item_name,
            i.weight,
            i.type,
            i.stat_str,
            i.stat_dex,
            i.stat_int,
            i.stat_hp,
            i.stat_ap,
            i.stat_atk,
            i.icon_path
        FROM
            character_items ci
        JOIN
            items i ON ci.item_id = i.id
        WHERE
            ci.character_id = ?
       """;

        return queryList(sql, statement -> statement.setInt(1, charId));
    }

    @Override
    public boolean equipItem(int instanceId, String slotName) {
        String sql = "UPDATE character_items SET slot_name = ?, grid_index = NULL WHERE instance_id = ?";

        return executeUpdate(
                sql,
                statement -> {
                    statement.setString(1, slotName);
                    statement.setInt(2, instanceId);
                }
        ) > 0;
    }

    @Override
    public boolean unequipItem(int instanceId, int newGridIndex) {
        String sql = "UPDATE character_items SET slot_name = NULL, grid_index = ? WHERE instance_id = ?";

        return executeUpdate(
                sql,
                statement -> {
                    statement.setInt(1, newGridIndex);
                    statement.setInt(2, instanceId);
                }
        ) > 0;
    }

    @Override
    public void addItemToCharacter(int charId, int itemId, int gridIndex) {
        String sql = "INSERT INTO character_items (character_id, item_id, slot_name, grid_index) VALUES (?, ?, NULL, ?)";

        executeInsert(
                sql,
                statement -> {
                    statement.setInt(1, charId);
                    statement.setInt(2, itemId);
                    statement.setInt(3, gridIndex);
                }
        );
    }

    @Override
    public boolean deleteItem(int instanceId) {
        String sql = "DELETE FROM character_items WHERE instance_id = ?";

        return executeUpdate(
                sql,
                statement -> statement.setInt(1, instanceId)
        ) > 0;
    }

    @Override
    protected InventoryItem mapRow(ResultSet result) throws SQLException {
        EquipmentSlot slotType;
        try {
            slotType = EquipmentSlot.valueOf(result.getString("type"));
        } catch (IllegalArgumentException e) {
            slotType = EquipmentSlot.MISC;
        }

        Item item = new Item(
                result.getInt("item_id"),
                result.getString("item_name"),
                result.getDouble("weight"),
                slotType,
                result.getInt("stat_str"),
                result.getInt("stat_dex"),
                result.getInt("stat_int"),
                result.getInt("stat_hp"),
                result.getInt("stat_ap"),
                result.getInt("stat_atk"),
                result.getString("icon_path")
        );

        return new InventoryItem(
                result.getInt("instance_id"),
                item,
                result.getString("slot_name"),
                result.getInt("grid_index")
        );
    }
}
