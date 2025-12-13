package com.project.charforge.service.interfaces;

import com.project.charforge.model.dto.StatSnapshot;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.service.impl.StatCalculator;

public interface IStatCalculator {
    StatSnapshot calculate(PlayerCharacter character);
}
