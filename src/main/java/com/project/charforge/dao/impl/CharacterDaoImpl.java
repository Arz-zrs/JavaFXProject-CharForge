package com.project.charforge.dao.impl;

import com.project.charforge.dao.base.BaseDao;
import com.project.charforge.dao.interfaces.CharClassDao;
import com.project.charforge.dao.interfaces.CharacterDao;
import com.project.charforge.dao.interfaces.RaceDao;
import com.project.charforge.model.entity.character.CharClass;
import com.project.charforge.model.entity.character.Gender;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.character.Race;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CharacterDaoImpl extends BaseDao<PlayerCharacter> implements CharacterDao {
    private final RaceDao raceDao;
    private final CharClassDao classDao;

    public CharacterDaoImpl(RaceDao raceDao, CharClassDao classDao) {
        this.raceDao = raceDao;
        this.classDao = classDao;
    }

    @Override
    public int save(PlayerCharacter character) {
        String sql = "INSERT INTO characters (name, race_id, job_class_id, gender) VALUES (?, ?, ?, ?)";

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
                    cl.bonus_str, cl.bonus_dex, cl.bonus_int
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
        character.setId(result.getInt("id"));
        character.setName(result.getString("name"));

        // Race object
        if (!result.wasNull()) {
            Race race = new Race(
                    result.getInt("race_id"),
                    result.getString("race_name"),
                    result.getInt("race_base_str"),
                    result.getInt("race_base_dex"),
                    result.getInt("race_base_int"),
                    result.getDouble("race_weight_capacity")
            );
            character.setRace(race);
        }

        // Class object
        if (!result.wasNull()) {
            CharClass charClass = new CharClass(
                    result.getInt("class_id"),
                    result.getString("class_name"),
                    result.getInt("bonus_str"),
                    result.getInt("bonus_dex"),
                    result.getInt("bonus_int")

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
