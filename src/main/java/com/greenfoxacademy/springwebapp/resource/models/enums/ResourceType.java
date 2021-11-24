package com.greenfoxacademy.springwebapp.resource.models.enums;

import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;

public enum ResourceType {
    FOOD("food", BuildingType.FARM),
    GOLD("gold", BuildingType.MINE);

    public final String resourceType;
    public final BuildingType buildingType;

    ResourceType(String resourceType, BuildingType buildingType) {
        this.resourceType = resourceType;
        this.buildingType = buildingType;
    }
}
