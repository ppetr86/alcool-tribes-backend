package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class PasswordMissingOrTooShortException extends RuntimeException {

  public PasswordMissingOrTooShortException() {
    super("Username is already taken.");
  }
}
