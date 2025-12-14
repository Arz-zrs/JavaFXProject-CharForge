package com.project.charforge.service.impl.stats;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.service.interfaces.stats.IEncumbranceService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;

public class EncumbranceService implements IEncumbranceService {
    private final IStatCalculator statCalculator;

    public EncumbranceService(IStatCalculator statCalculator) {
        this.statCalculator = statCalculator;
    }

    @Override
    public double getCurrentWeight(PlayerCharacter character) {
        return character.getInventory().stream()
                .mapToDouble(i -> i.getItem().getWeight())
                .sum();
    }

    @Override
    public double getMaxWeight(PlayerCharacter character) {
        int str = statCalculator.calculate(character).totalStr();
        return (50 + str * 2) * character.getRace().getWeightModifier();
    }
}
