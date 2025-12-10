package com.project.charforge.dao.interfaces;

import com.project.charforge.model.entity.character.CharClass;

import java.util.List;

public interface CharClassDao {
    List<CharClass> findAll();
}
