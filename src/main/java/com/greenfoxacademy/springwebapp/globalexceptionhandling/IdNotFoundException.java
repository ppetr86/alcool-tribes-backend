package com.greenfoxacademy.springwebapp.globalexceptionhandling;


public class IdNotFoundException extends RuntimeException {

  public IdNotFoundException() {
    super("Id not found");
  }
}
