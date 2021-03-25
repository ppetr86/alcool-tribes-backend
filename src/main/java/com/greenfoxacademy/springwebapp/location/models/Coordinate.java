package com.greenfoxacademy.springwebapp.location.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coordinate {
  private int x;
  private int y;
  private Coordinate parent;
  private LocationEntity location;

  public Coordinate(int x, int y, LocationEntity locationEntity) {
    this.x = x;
    this.y = y;
    this.parent = null;
    this.location = locationEntity;
  }
}
