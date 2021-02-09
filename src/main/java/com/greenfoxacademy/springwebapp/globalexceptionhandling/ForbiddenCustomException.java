package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class ForbiddenCustomException extends RuntimeException {
  public ForbiddenCustomException() {
    super("Forbidden action");
  }
}
