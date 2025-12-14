package com.project.charforge.service.interfaces.stats;

import com.project.charforge.model.dto.StatSnapshot;
import com.project.charforge.model.entity.character.PlayerCharacter;

public interface IStatCalculator {
    StatSnapshot calculate(PlayerCharacter character);
}
