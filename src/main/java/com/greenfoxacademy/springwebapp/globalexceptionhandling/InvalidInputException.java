package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class InvalidInputException extends RuntimeException {

  public InvalidInputException(String message) {
    super("Invalid " + message);
  }
}