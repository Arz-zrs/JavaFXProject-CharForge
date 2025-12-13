package com.project.charforge.dao.interfaces;

import com.project.charforge.model.entity.character.PlayerCharacter;

import java.util.List;
import java.util.Optional;

public interface CharacterDao {
    List<PlayerCharacter> findAll();
    int save(PlayerCharacter character);
    Optional<PlayerCharacter> findById(int id);
}
