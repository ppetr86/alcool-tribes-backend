package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class InvalidTokenException extends Exception {

    public InvalidTokenException() {
        super("Token is not valid");
    }
}
