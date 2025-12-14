package com.project.charforge.service.interfaces.characters;

import com.project.charforge.model.dto.CharacterSnapshot;
import com.project.charforge.model.entity.character.PlayerCharacter;

public interface ICharacterStatService {
    CharacterSnapshot snapshot(PlayerCharacter character);
}
