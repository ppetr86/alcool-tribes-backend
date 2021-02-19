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
            new BuildingEntity(1L, BuildingType.TOWNHALL, 1, 100, 100L, 200L, kingdom),
            new BuildingEntity(2L, BuildingType.ACADEMY, 1, 100, 100L, 200L, kingdom),
            new BuildingEntity(3L, BuildingType.FARM, 1, 100, 100L, 200L, kingdom),
            new BuildingEntity(4L, BuildingType.MINE, 1, 100, 100L, 200L, kingdom)
    );
  }
  public static List<BuildingEntity> createDefaultLevel1BuildingsWithAllData() {
    return Arrays.asList(
        new BuildingEntity(1L,BuildingType.TOWNHALL,1,200,1613303221,1613303341),
        new BuildingEntity(2L,BuildingType.FARM,1,100,1613303221,1613303281),
        new BuildingEntity(3L,BuildingType.MINE,1,100,1613303221,1613303281),
        new BuildingEntity(4L,BuildingType.ACADEMY,1,150,1613303221,1613303371)
    );
  }

}
