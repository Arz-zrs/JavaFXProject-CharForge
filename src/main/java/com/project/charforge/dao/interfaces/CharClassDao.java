package com.project.charforge.dao.interfaces;

import com.project.charforge.model.entity.character.CharClass;

import java.util.List;
import java.util.Optional;

public interface CharClassDao {
    List<CharClass> findAll();
    Optional<CharClass> findById(int classId);
}
