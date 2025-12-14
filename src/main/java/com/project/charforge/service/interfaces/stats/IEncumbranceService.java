package com.project.charforge.service.interfaces.stats;

import com.project.charforge.model.entity.character.PlayerCharacter;

public interface IEncumbranceService {
    double getCurrentWeight(PlayerCharacter character);
    double getMaxWeight(PlayerCharacter character, int totalStr);
}
