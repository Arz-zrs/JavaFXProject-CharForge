package com.project.charforge.service.interfaces.characters;

import com.project.charforge.model.entity.character.CharClass;
import com.project.charforge.model.entity.character.Gender;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.character.Race;

import java.util.List;

public interface ICharacterService {
    PlayerCharacter createCharacter(
            String name,
            Gender gender,
            Race race,
            CharClass charClass
    );
    void saveCharacterToDB(PlayerCharacter character);
    List<Race> getAllRaces();
    List<CharClass> getAllClasses();
    List<PlayerCharacter> findAllCharacters();
    boolean deleteCharacter(int characterId);
}
