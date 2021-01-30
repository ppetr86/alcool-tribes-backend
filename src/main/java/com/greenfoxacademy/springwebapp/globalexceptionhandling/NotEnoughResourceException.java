package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class NotEnoughResourceException extends Exception {

  public NotEnoughResourceException() {
    super("Not enough resource");
  }
}
