package com.project.charforge.model.dto;

public record StatSnapshot(
        int totalStr,
        int totalDex,
        int totalInt,
        double currentWeight,
        double maxWeight,
        boolean isOverweight
){}