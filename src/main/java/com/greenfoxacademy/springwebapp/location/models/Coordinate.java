package com.greenfoxacademy.springwebapp.location.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Coordinate {
  protected int x;
  protected int y;
  protected Coordinate parent;

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
    this.parent = null;
  }
}
