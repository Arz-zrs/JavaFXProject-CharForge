package com.project.charforge.service.impl.characters;

import com.project.charforge.dao.interfaces.CharacterDao;
import com.project.charforge.service.interfaces.characters.ICharacterService;

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
