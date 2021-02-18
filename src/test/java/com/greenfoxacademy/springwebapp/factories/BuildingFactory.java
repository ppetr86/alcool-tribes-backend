package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;

import java.util.Arrays;
import java.util.List;

public class BuildingFactory {

  public static List<BuildingEntity> createDefaultBuildings() {
    return Arrays.asList(
            new BuildingEntity(null, BuildingType.TOWNHALL, 1),
            new BuildingEntity(null, BuildingType.ACADEMY, 1),
            new BuildingEntity(null, BuildingType.FARM, 1),
            new BuildingEntity(null, BuildingType.MINE, 1)
    );
  }


  public static List<BuildingEntity> createBuildings(KingdomEntity kingdom) {
    return Arrays.asList(
            new BuildingEntity(1L, BuildingType.TOWNHALL, 1, 100, 999L, 1111L, kingdom),
            new BuildingEntity(2L, BuildingType.MINE, 1, 100, 999L, 1111L, kingdom)
    );
  }

}
