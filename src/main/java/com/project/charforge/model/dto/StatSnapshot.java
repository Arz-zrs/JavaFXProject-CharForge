package com.project.charforge.model.dto;

public record StatSnapshot(
        DerivedStat str,
        DerivedStat dex,
        DerivedStat ints,
        DerivedStat hp,
        DerivedStat ap,
        DerivedStat atk,
        double currentWeight,
        double maxWeight,
        boolean isEncumbered
){ }