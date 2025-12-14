package com.project.charforge.model.dto;

public record DerivedStat(int base, int bonus) {
    public int total() {
        return base + bonus;
    }
}
