package com.greenfoxacademy.springwebapp.models;

public enum BuildingType {
  TOWNHALL("townhall"),
  FARM("farm"),
  MINE("mine"),
  ACADEMY("academy");

  public final String label;

  private BuildingType(String label) {
    this.label = label;
  }
}
