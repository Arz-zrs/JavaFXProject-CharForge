package com.project.charforge.dao.interfaces;

import com.project.charforge.model.entity.character.PlayerCharacter;

import java.util.List;

public interface CharacterDao {
    List<PlayerCharacter> findAll();
    int save(PlayerCharacter character);
    boolean delete(int id);
}
