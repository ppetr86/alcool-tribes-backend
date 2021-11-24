package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class MissingParameterException extends RuntimeException {

    public MissingParameterException(String str) {
        super("Missing parameter(s): " + str + "!");
    }
}