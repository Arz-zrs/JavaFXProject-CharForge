package com.project.charforge.dao.impl;

import com.project.charforge.dao.base.BaseDao;
import com.project.charforge.dao.interfaces.CharacterDao;
import com.project.charforge.db.SQLiteConnectionProvider;
import com.project.charforge.model.entity.character.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CharacterDaoImpl extends BaseDao<PlayerCharacter> implements CharacterDao {
    @Override
    public List<PlayerCharacter> findAll() {
        String sql = """
                SELECT
                    c.id AS char_id, c.name AS char_name, c.gender AS char_gender,
                    r.id AS race_id, r.name AS race_name, r.base_str, r.base_dex, r.base_int, r.weight_capacity_modifier,
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
        String deleteInventorySql =
                "DELETE FROM character_items WHERE character_id = ?";
        String deleteCharacterSql =
                "DELETE FROM characters WHERE id = ?";

        try (Connection conn = SQLiteConnectionProvider.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psInv = conn.prepareStatement(deleteInventorySql);
                 PreparedStatement psChar = conn.prepareStatement(deleteCharacterSql)) {

                psInv.setInt(1, id);
                psInv.executeUpdate();

                psChar.setInt(1, id);
                int affectedRows = psChar.executeUpdate();

                conn.commit();
                return affectedRows > 0;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete character " + id, e);
        }
    }

    @Override
    public Optional<PlayerCharacter> findById(int id) {
        String sql = """
                SELECT
                    c.id AS char_id,
                    c.name AS char_name,
                    c.gender AS char_gender,
                
                    r.id AS race_id,
                    r.name AS race_name,
                    r.base_str, r.base_dex, r.base_int,
                    r.weight_capacity_modifier,
                
                    cl.id AS class_id,
                    cl.name AS class_name,
                    cl.bonus_str, cl.bonus_dex, cl.bonus_int, cl.attack_scaling
                FROM characters c
                LEFT JOIN races r ON c.race_id = r.id
                LEFT JOIN classes cl ON c.class_id = cl.id
                WHERE c.id = ?
                """;

        PlayerCharacter character = querySingle(sql, statement -> statement.setInt(1, id));
        return Optional.ofNullable(character);
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
                e.getStackTrace();
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
