package com.project.charforge.model.service.interfaces;

import com.project.charforge.model.entity.character.CharClass;
import com.project.charforge.model.entity.character.Gender;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.character.Race;

public interface ICharacterCreationService {
    PlayerCharacter createCharacter(
            String name,
            Gender gender,
            Race race,
            CharClass charClass
    );
}
