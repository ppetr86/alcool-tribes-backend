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

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
    this.parent = null;
  }
}
