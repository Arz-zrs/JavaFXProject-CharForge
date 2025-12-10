package com.project.charforge.dao.impl;

import com.project.charforge.dao.base.BaseDao;
import com.project.charforge.dao.interfaces.RaceDao;
import com.project.charforge.model.entity.character.Race;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class RaceDaoImpl extends BaseDao<Race> implements RaceDao {

    @Override
    public List<Race> findAll() {
        return queryList("SELECT * FROM races", StatementBinder.empty());
    }

    @Override
    public Optional<Race> findById(int id) {
        Race race = querySingle(
                "SELECT * FROM races WHERE id = ?",
                statement -> statement.setInt(1, id)
        );
        return Optional.ofNullable(race);
    }

    @Override
    protected Race mapRow(ResultSet result) throws SQLException {
        return new Race(
                result.getInt("id"),
                result.getString("name"),
                result.getInt("base_str"),
                result.getInt("base_dex"),
                result.getInt("base_int"),
                result.getDouble("weight_capacity_modifier")
        );
    }
}
