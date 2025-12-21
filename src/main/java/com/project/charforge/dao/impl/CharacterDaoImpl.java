package com.project.charforge.dao.impl;

import com.project.charforge.dao.base.BaseDao;
import com.project.charforge.dao.interfaces.CharacterDao;
import com.project.charforge.db.ConnectionProvider;
import com.project.charforge.model.entity.character.*;
import com.project.charforge.utils.Logs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CharacterDaoImpl extends BaseDao<PlayerCharacter> implements CharacterDao {
    public CharacterDaoImpl(ConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public List<PlayerCharacter> findAll() {
        String sql = """
                SELECT
                    c.id AS char_id, c.name AS char_name, c.gender AS char_gender,
                    r.id AS race_id, r.name AS race_name, r.base_hp, r.base_str, r.base_dex, r.base_int, r.weight_capacity_modifier,
                    cl.id AS class_id, cl.name AS class_name, cl.bonus_str, cl.bonus_dex, cl.bonus_int, cl.attack_scaling
                FROM characters c
                LEFT JOIN races r ON c.race_id = r.id
                LEFT JOIN classes cl ON c.class_id = cl.id
                """;
        return queryList(sql, StatementBinder.empty());
    }

    @Override
    public int save(PlayerCharacter character) {
        String sql = "INSERT INTO characters (name, race_id, class_id, gender) VALUES (?, ?, ?, ?)";

        return executeInsert(
                sql,
                statement -> {
                    statement.setString(1, character.getName());
                    statement.setInt(2, character.getRace().getId());
                    statement.setInt(3, character.getCharClass().getId());
                    statement.setString(4, character.getGender().name());
                }
        );
    }

    @Override
    public boolean delete(int id) {
        return executeUpdate(
                "DELETE FROM characters WHERE id = ?",
                stmt -> stmt.setInt(1, id)
        ) > 0;
    }

    @Override
    protected PlayerCharacter mapRow(ResultSet result) throws SQLException {
        PlayerCharacter character = new PlayerCharacter();

        // Character columns
        character.setId(result.getInt("char_id"));
        character.setName(result.getString("char_name"));

        // Race object
        if (!result.wasNull()) {
            Race race = new Race(
                    result.getInt("race_id"),
                    result.getString("race_name"),
                    result.getInt("base_hp"),
                    result.getInt("base_str"),
                    result.getInt("base_dex"),
                    result.getInt("base_int"),
                    result.getDouble("weight_capacity_modifier")
            );
            character.setRace(race);
        }

        // Class object
        String scalingStr = result.getString("attack_scaling");
        AttackScaling scaling = AttackScaling.STR;

        if (scalingStr != null) {
            try {
                scaling = AttackScaling.valueOf(scalingStr);
            } catch (IllegalArgumentException e) {
                Logs.printError("CharacterDao Error", e);
            }
        }

        if (!result.wasNull()) {
            CharClass charClass = new CharClass(
                    result.getInt("class_id"),
                    result.getString("class_name"),
                    result.getInt("bonus_str"),
                    result.getInt("bonus_dex"),
                    result.getInt("bonus_int"),
                    scaling
            );
            character.setCharClass(charClass);
        }

        // Gender enum
        String genderStr = result.getString("char_gender");
        if (genderStr != null) {
            character.setGender(Gender.valueOf(genderStr));
        } else {
            character.setGender(Gender.MALE);
        }

        return character;
    }
}
