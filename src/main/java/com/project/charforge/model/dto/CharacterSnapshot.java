package com.project.charforge.model.dto;

public record CharacterSnapshot(
        int totalStr,
        int totalDex,
        int totalInt,
        double currentWeight,
        double maxWeight,
        boolean isEncumbered
) {}
