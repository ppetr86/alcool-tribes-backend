package com.greenfoxacademy.springwebapp.building.models.enums;

import java.io.Serializable;

public enum BuildingType implements Serializable {
    TOWNHALL("townhall"),
    FARM("farm"),
    MINE("mine"),
    ACADEMY("academy");

    public final String buildingType;

    BuildingType(String buildingType) {
        this.buildingType = buildingType;

    }
}
