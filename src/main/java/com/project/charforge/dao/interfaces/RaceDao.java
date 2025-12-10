package com.project.charforge.dao.interfaces;

import com.project.charforge.model.entity.character.Race;

import java.util.List;
import java.util.Optional;

public interface RaceDao {
    List<Race> findAll();
    Optional<Race> findById(int id);
}
