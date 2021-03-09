package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class ForbiddenActionException extends RuntimeException {

  public ForbiddenActionException() {
    super("Forbidden action");
  }
}

