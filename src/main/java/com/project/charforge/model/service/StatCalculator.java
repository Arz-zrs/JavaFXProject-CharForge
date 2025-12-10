package com.project.charforge.model.service;

import com.project.charforge.model.entity.base.StatModifier;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.item.Item;

import java.util.ArrayList;
import java.util.List;

public class StatCalculator {
    public record StatSnapshot(
            int totalStr,
            int totalDex,
            int totalInt,
            double currentWeight,
            double maxWeight,
            boolean isOverweight
    ){}

    public StatSnapshot calculate(PlayerCharacter character) {
        int strength = 0;
        int dexterity = 0;
        int intelligence = 0;
        double currentWeight = 0.0;

        List<StatModifier> modifiers = new ArrayList<>();

        if (character.getRace() != null) modifiers.add(character.getRace());
        if (character.getCharClass() != null) modifiers.add(character.getCharClass());
        modifiers.addAll(character.getEquipment().values());

        for (StatModifier mod: modifiers) {
            strength += mod.getStrBonus();
            dexterity += mod.getDexBonus();
            intelligence += mod.getIntBonus();

            if (mod instanceof Item item) currentWeight += item.getWeight();
        }

        double baseCap = 50.0;
        double maxWeight = 0;

        if (character.getRace() != null) {
            maxWeight = (baseCap + (strength * 2)) * character.getRace().getWeightModifier();
        }
        return new StatSnapshot(strength, dexterity, intelligence, currentWeight, maxWeight, currentWeight > maxWeight);
    }
}
