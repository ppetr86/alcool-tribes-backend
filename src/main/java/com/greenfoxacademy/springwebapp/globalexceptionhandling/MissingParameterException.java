package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class MissingParameterException extends Exception {

  public MissingParameterException() {
    super("Missing parameter(s): type!");
  }
}
