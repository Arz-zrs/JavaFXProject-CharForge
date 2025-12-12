package com.project.charforge.dao.interfaces;

import com.project.charforge.model.entity.character.PlayerCharacter;

import java.util.Optional;

public interface CharacterDao {
    int save(PlayerCharacter character);
    Optional<PlayerCharacter> findById(int id);
}
