package com.project.charforge.dao.impl;

import com.project.charforge.dao.base.BaseDao;
import com.project.charforge.dao.interfaces.CharClassDao;
import com.project.charforge.model.entity.character.CharClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CharClassDaoImpl extends BaseDao<CharClass> implements CharClassDao {
    @Override
    public List<CharClass> findAll() {
        return queryList("SELECT * FROM classes", StatementBinder.empty());
    }

    @Override
    protected CharClass mapRow(ResultSet result) throws SQLException {
        return new CharClass(
                result.getInt("id"),
                result.getString("name"),
                result.getInt("bonus_str"),
                result.getInt("bonus_dex"),
                result.getInt("bonus_int")
        );
    }
}
