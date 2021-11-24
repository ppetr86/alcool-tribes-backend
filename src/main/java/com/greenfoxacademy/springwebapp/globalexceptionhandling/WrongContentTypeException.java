package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class WrongContentTypeException extends RuntimeException {

    public WrongContentTypeException(String str) {
        super(str);
    }

}
