package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class MissingParameterException extends Exception {

  public MissingParameterException(String str) {

    super("Missing parameter(s): " + str + "!");
  }
}
