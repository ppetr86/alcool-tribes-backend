package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class MyFileNotFoundException extends RuntimeException {

  public MyFileNotFoundException(String str) {
    super(str);
  }

}
