package com.project.charforge.model.service.interfaces;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.service.impl.StatCalculator;

public interface IStatCalculator {
    StatCalculator.StatSnapshot calculate(PlayerCharacter character);
}
