package com.greenfoxacademy.springwebapp.battle.models.enums;

public enum ArmyType {
    ATTACKINGARMY("attackingArmy"),
    DEFENDINGARMY("defendingArmy");

    public final String armyType;

    ArmyType(String armyType) {
        this.armyType = armyType;
    }
}
