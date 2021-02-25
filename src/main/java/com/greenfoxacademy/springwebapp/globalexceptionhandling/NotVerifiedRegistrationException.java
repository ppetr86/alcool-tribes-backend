package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class NotVerifiedRegistrationException extends Throwable {

  public NotVerifiedRegistrationException() {
    super("Not verified username.");
  }

}
