package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class NotEnoughResourceException extends RuntimeException {

    public NotEnoughResourceException() {
        super("Not enough resource");
    }
}
