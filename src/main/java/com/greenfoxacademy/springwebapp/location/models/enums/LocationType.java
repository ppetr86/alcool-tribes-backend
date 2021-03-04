package com.greenfoxacademy.springwebapp.location.models.enums;

public enum LocationType {
  KINGDOM("kingdom"),
  DESERT("desert"),
  JUNGLE("jungle"),
  EMPTY("empty");

  public final String locationType;

  LocationType(String locationType) {
    this.locationType = locationType;

  }
}
