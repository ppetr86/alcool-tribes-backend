package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class InvalidBuildingTypeException extends RuntimeException {

    public InvalidBuildingTypeException() {
        super("Invalid building type");
    }
}