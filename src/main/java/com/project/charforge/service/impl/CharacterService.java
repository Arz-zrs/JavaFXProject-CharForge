package com.project.charforge.service.impl;

import com.project.charforge.dao.interfaces.CharacterDao;
import com.project.charforge.service.interfaces.ICharacterService;

public class CharacterService implements ICharacterService {
    private final CharacterDao characterDao;

    public CharacterService(CharacterDao characterDao) {
        this.characterDao = characterDao;
    }

    @Override
    public boolean deleteCharacter(int characterId) {
        return characterDao.delete(characterId);
    }
}
