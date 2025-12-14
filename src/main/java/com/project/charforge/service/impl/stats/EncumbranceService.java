package com.project.charforge.service.impl.stats;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.service.interfaces.stats.IEncumbranceService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;

public class EncumbranceService implements IEncumbranceService {
    private static final double BASE_WEIGHT_CAP = 50.0;
    private static final int WEIGHT_PER_STR = 2;

    @Override
    public double getCurrentWeight(PlayerCharacter character) {
        return character.getInventory().stream()
                .mapToDouble(i -> i.getItem().getWeight())
                .sum();
    }

    @Override
    public double getMaxWeight(PlayerCharacter character, int totalStr) {
        return (BASE_WEIGHT_CAP + (totalStr * WEIGHT_PER_STR))
                * character.getRace().getWeightModifier();
    }
}
