package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class PasswordIsRequiredException extends RuntimeException {

  public PasswordIsRequiredException() {
    super("Password is required.");
  }
}
