package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class InvalidBuildingTypeException extends Exception {

  public InvalidBuildingTypeException() {
    super("Invalid building type || Cannot build buildings with higher level than the Townhall");
  }
}
