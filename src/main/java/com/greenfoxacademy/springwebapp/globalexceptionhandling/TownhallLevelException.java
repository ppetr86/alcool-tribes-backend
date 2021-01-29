package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class TownhallLevelException extends Exception {

  public TownhallLevelException() {
    super("Cannot build buildings with higher level than the Townhall");
  }
}
