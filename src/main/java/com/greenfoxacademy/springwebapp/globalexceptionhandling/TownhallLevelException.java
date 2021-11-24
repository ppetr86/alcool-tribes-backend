package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class TownhallLevelException extends RuntimeException {

  public TownhallLevelException() {
    super("Cannot build buildings with higher level than the Townhall");
  }
}
