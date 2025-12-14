package com.project.charforge.service.impl.characters;

import com.project.charforge.dao.interfaces.CharacterDao;
import com.project.charforge.model.entity.character.CharClass;
import com.project.charforge.model.entity.character.Gender;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.character.Race;
import com.project.charforge.service.interfaces.characters.ICharacterCreationService;

public class CharacterCreationService implements ICharacterCreationService {
    private final CharacterDao characterDao;

    public CharacterCreationService(CharacterDao characterDao) {
        this.characterDao = characterDao;
    }

    @Override
    public PlayerCharacter createCharacter(String name, Gender gender, Race race, CharClass charClass) {
        PlayerCharacter pc = new PlayerCharacter();
        pc.setName(name);
        pc.setGender(gender);
        pc.setRace(race);
        pc.setCharClass(charClass);

        int id = characterDao.save(pc);
        if (id == -1) {
            throw new RuntimeException("Failed to save character");
        }

        pc.setId(id);

        return pc;
    }
}
