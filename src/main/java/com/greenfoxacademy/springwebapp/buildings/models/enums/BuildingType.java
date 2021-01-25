package com.greenfoxacademy.springwebapp.buildings.models.enums;

public enum BuildingType {
  TOWNHALL("townhall"),
  FARM("farm"),
  MINE("mine"),
  ACADEMY("academy");

  public final String buildingType;

  BuildingType(String buildingType) {
    this.buildingType = buildingType;

  }
}
