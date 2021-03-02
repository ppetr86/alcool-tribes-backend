package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class UsernameIsTakenException extends RuntimeException {

  public UsernameIsTakenException() {
    super("Username is already taken.");
  }
}