package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class IncorrectUsernameOrPwdException extends Throwable {

  public IncorrectUsernameOrPwdException() {
    super("Username or password is incorrect.");
  }
}