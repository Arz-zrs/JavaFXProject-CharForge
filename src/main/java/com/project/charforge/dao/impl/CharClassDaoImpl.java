package com.project.charforge.dao.impl;

import com.project.charforge.dao.base.BaseDao;
import com.project.charforge.dao.interfaces.CharClassDao;
import com.project.charforge.model.entity.character.AttackScaling;
import com.project.charforge.model.entity.character.CharClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CharClassDaoImpl extends BaseDao<CharClass> implements CharClassDao {
    @Override
    public List<CharClass> findAll() {
        String sql = "SELECT * FROM classes";

        return queryList(sql, StatementBinder.empty());
    }

    @Override
    public Optional<CharClass> findById(int classId) {
        String sql = "SELECT * FROM classes WHERE id = ?";

        CharClass charClass = querySingle(
                sql,
                statement -> statement.setInt(1, classId)
        );
        return Optional.ofNullable(charClass);
    }

    @Override
    protected CharClass mapRow(ResultSet result) throws SQLException {
        String scalingStr = result.getString("attack_scaling");
        AttackScaling scaling = AttackScaling.STR;

        if (scalingStr != null) {
            try {
                scaling = AttackScaling.valueOf(scalingStr);
            } catch (IllegalArgumentException e) {
                e.getStackTrace();
            }
        }
        
        return new CharClass(
                result.getInt("id"),
                result.getString("name"),
                result.getInt("bonus_str"),
                result.getInt("bonus_dex"),
                result.getInt("bonus_int"),
                scaling
        );
    }
}
