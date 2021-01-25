package com.greenfoxacademy.springwebapp.building.models.enums;

public enum BuildingType {
  TOWNHALL("townhall", 120, 200),
  FARM("farm", 60, 100),
  MINE("mine", 60, 100),
  ACADEMY("academy", 90, 150);

  public final String buildingType;
  public final int buildTime;
  public final int hp;

  BuildingType(String buildingType, int buildTime, int hp) {
    this.buildingType = buildingType;
    this.buildTime = buildTime;
    this.hp = hp;
  }
}
