package com.greenfoxacademy.springwebapp.buildings.models.enums;

public enum BuildingType {
  TOWNHALL("townhall"),
  FARM("farm"),
  MINE("mine"),
  ACADEMY("academy");

  public final String label;

  BuildingType(String label) {
    this.label = label;
  }
}
