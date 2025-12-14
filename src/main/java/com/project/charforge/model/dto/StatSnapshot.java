package com.project.charforge.model.dto;

public record StatSnapshot(
        int totalStr,
        int totalDex,
        int totalInt,
        int healthPoints,
        int armorPoints,
        int attack,
        double currentWeight,
        double maxWeight,
        boolean isEncumbered
){ }