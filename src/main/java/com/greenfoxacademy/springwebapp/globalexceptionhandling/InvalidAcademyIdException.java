package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class InvalidAcademyIdException extends RuntimeException {

  public InvalidAcademyIdException() {
    super("Not a valid academy id");
  }
}