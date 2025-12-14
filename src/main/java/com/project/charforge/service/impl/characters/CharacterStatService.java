package com.project.charforge.service.impl.characters;

import com.project.charforge.model.dto.CharacterSnapshot;
import com.project.charforge.model.dto.StatSnapshot;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.service.interfaces.characters.ICharacterStatService;
import com.project.charforge.service.interfaces.stats.IEncumbranceService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;

public class CharacterStatService implements ICharacterStatService {
    private final IStatCalculator statCalculator;
    private final IEncumbranceService encumbranceService;

    public CharacterStatService(IStatCalculator statCalculator, IEncumbranceService encumbranceService) {
        this.statCalculator = statCalculator;
        this.encumbranceService = encumbranceService;
    }

    @Override
    public CharacterSnapshot snapshot(PlayerCharacter character) {
        StatSnapshot stats = statCalculator.calculate(character);

        double currentWeight = encumbranceService.getCurrentWeight(character);
        double maxWeight = encumbranceService.getMaxWeight(character);

        return new CharacterSnapshot(
                stats.totalStr(),
                stats.totalDex(),
                stats.totalInt(),
                currentWeight,
                maxWeight,
                currentWeight > maxWeight
        );
    }
}
