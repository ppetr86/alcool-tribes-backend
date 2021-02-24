package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class UsernameAndPasswordRequiredException extends RuntimeException {

  public UsernameAndPasswordRequiredException() {
    super("Username and password are required.");
  }
}
