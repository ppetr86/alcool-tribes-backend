package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class ForbiddenException extends RuntimeException {
  public ForbiddenException(String message) {
    super(message);
  }
}
